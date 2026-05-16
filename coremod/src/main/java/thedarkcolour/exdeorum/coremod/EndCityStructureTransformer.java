package thedarkcolour.exdeorum.coremod;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Set;

// inserts a hook into EndCityStructure#findGenerationPoint to fix the position of the city if it is in a void world
public class EndCityStructureTransformer extends SimpleMethodProcessor {
    private static final ProcessorName NAME = new ProcessorName("exdeorum", "end_city_structure_void_transformer");

    @Override
    public ProcessorName name() {
        return NAME;
    }

    @Override
    public Set<Target> targets() {
        return Set.of(new Target(
                "net.minecraft.world.level.levelgen.structure.structures.EndCityStructure",
                "findGenerationPoint",
                "(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/util/Optional;"
        ));
    }

    @Override
    public void transform(MethodNode input, SimpleTransformationContext context) {
        var insnList = input.instructions;

        for (var i = 0; i < insnList.size(); ++i) {
            var insn = insnList.get(i);

            // patch before ASTORE 3
            if (insn.getOpcode() == Opcodes.ASTORE && ((VarInsnNode) insn).var == 3) {
                insnList.insertBefore(insn, ExDeorumASM.insnList(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(Opcodes.INVOKESTATIC, "thedarkcolour/exdeorum/asm/ASMHooks", "adjustPos", "(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Lnet/minecraft/core/BlockPos;", false)
                ));
                ExDeorumASM.LOGGER.info("Successfully patched End City generation for void worlds");
                return;
            }
        }

        ExDeorumASM.LOGGER.error("Unable to patch End City generation, void worlds will have no end cities!!!");
    }
}
