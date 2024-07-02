/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;
import java.util.Set;

public class ASMTransformer implements ICoreMod {
    @Override
    public Iterable<? extends ITransformer<?>> getTransformers() {
        return List.of(
                new EndCityStructureTransformer(),
                new DedicatedServerPropertiesTransformer(),
                new EndDragonFightTransformer()
        );
    }

    private interface MethodTransformer extends ITransformer<MethodNode> {
        @Override
        default TransformerVoteResult castVote(ITransformerVotingContext context) {
            return TransformerVoteResult.YES;
        }

        @Override
        default TargetType<MethodNode> getTargetType() {
            return TargetType.METHOD;
        }
    }

    // inserts a hook into EndCityStructure#findGenerationPoint to fix the position of the city if it is in a void world
    private static class EndCityStructureTransformer implements MethodTransformer {
        @Override
        public Set<Target<MethodNode>> targets() {
            return Set.of(ITransformer.Target.targetMethod(
                    "net.minecraft.world.level.levelgen.structure.structures.EndCityStructure",
                    "findGenerationPoint",
                    "(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/util/Optional;"
            ));
        }

        //
        @Override
        public MethodNode transform(MethodNode input, ITransformerVotingContext context) {
            var insnList = input.instructions;

            for (var i = 0; i < insnList.size(); ++i) {
                var insn = insnList.get(i);

                // patch before ASTORE 3
                if (insn.getOpcode() == Opcodes.ASTORE && ((VarInsnNode) insn).var == 3) {
                    insnList.insertBefore(insn, ASMAPI.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 1),
                            new MethodInsnNode(Opcodes.INVOKESTATIC, "thedarkcolour/exdeorum/asm/ASMHooks", "adjustPos", "(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Lnet/minecraft/core/BlockPos;", false)
                    ));
                    ASMAPI.log("INFO", "Successfully patched End City generation for void worlds");
                    return input;
                }
            }

            ASMAPI.log("ERROR", "Unable to patch End City generation, void worlds will have no end cities!!!");
            return input;
        }
    }

    // Redirects a field access in the constructor of DedicatedServerProperties from WorldPresets.NORMAL to ASMHooks.overrideDefaultWorldPreset()
    private static class DedicatedServerPropertiesTransformer implements MethodTransformer {
        @Override
        public Set<Target<MethodNode>> targets() {
            return Set.of(ITransformer.Target.targetMethod(
                    "net.minecraft.server.dedicated.DedicatedServerProperties",
                    "<init>",
                    "(Ljava/util/Properties;)V"
            ));
        }

        @Override
        public MethodNode transform(MethodNode input, ITransformerVotingContext context) {
            var insnList = input.instructions;

            for (var i = 0; i < insnList.size(); ++i) {
                var insn = insnList.get(i);

                if (insn.getOpcode() == Opcodes.GETSTATIC && (((MethodInsnNode) insn).name.equals("f_226437_") || ((MethodInsnNode) insn).name.equals("NORMAL"))) {
                    var newInsn = new MethodInsnNode(Opcodes.INVOKESTATIC, "thedarkcolour/exdeorum/asm/ASMHooks", "overrideDefaultWorldPreset", "()Lnet/minecraft/resources/ResourceKey;", false);
                    insnList.set(insn, newInsn);

                    ASMAPI.log("INFO", "Successfully patched server.properties to use void world type by default");
                    return input;
                }
            }

            ASMAPI.log("ERROR", "Unable to patch server.properties, you will have to set \"level-type\" to \"exdeorum:void_world\" manually.");
            return input;
        }
    }

    // Fixes heightmap issues when placing the end portal podium that would only spawn half of the portal
    // What this patch looks like in code: EndIslandPodium.place(..., this.portalLocation = ASMHooks.prePlaceEndPodium(this.portalLocation))
    private static class EndDragonFightTransformer implements MethodTransformer {
        @Override
        public Set<Target<MethodNode>> targets() {
            return Set.of(ITransformer.Target.targetMethod(
                    "net.minecraft.world.level.dimension.end.EndDragonFight",
                    "spawnExitPortal",
                    "(Z)V"
            ));
        }

        @Override
        public MethodNode transform(MethodNode input, ITransformerVotingContext context) {
            var insnList = input.instructions;

            // Start at 2 to avoid null getPrevious().getPrevious()
            for (var i = 2; i < insnList.size(); ++i) {
                var insn = insnList.get(i);

                if (insn.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) insn).var == 2) {
                    insnList.insertBefore(insn, ASMAPI.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/level/dimension/end/EndDragonFight", "portalLocation", "Lnet/minecraft/core/BlockPos;"),
                            new MethodInsnNode(Opcodes.INVOKESTATIC, "thedarkcolour/exdeorum/asm/ASMHooks", "prePlaceEndPodium", "(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;", false),
                            new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/level/dimension/end/EndDragonFight", "portalLocation", "Lnet/minecraft/core/BlockPos;")
                    ));
                    ASMAPI.log("INFO", "Successfully patched end portal.");
                    return input;
                }
            }

            ASMAPI.log("ERROR", "Unable to patch End Portal, it will not spawn properly and you will be unable to return to the overworld without cheats.");
            return input;
        }
    }
}
