// Type definitions

var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

var ASMAPI = Java.type('net.neoforged.coremod.api.ASMAPI');

function initializeCoreMod() {
    return {
        // inserts a hook into EndCityStructure#findGenerationPoint to fix the position of the city if it is in a void world
        'EndCityPatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.levelgen.structure.structures.EndCityStructure',
                'methodName': 'findGenerationPoint',
                'methodDesc': '(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/util/Optional;'
            },
            'transformer': function (method) {
                var insnList = method.instructions;

                for (var i = 0; i < insnList.size(); ++i) {
                    var insn = insnList.get(i);

                    // patch before ASTORE 3
                    if (insn.getOpcode() === Opcodes.ASTORE && insn.var === 3) {
                        insnList.insertBefore(insn, ASMAPI.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 1),
                            new MethodInsnNode(Opcodes.INVOKESTATIC, 'thedarkcolour/exdeorum/asm/ASMHooks', 'adjustPos', '(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Lnet/minecraft/core/BlockPos;', false)
                        ));
                        ASMAPI.log('INFO', 'Successfully patched End City generation for void worlds');
                        return method;
                    }
                }

                ASMAPI.log('ERROR', 'Unable to patch End City generation, void worlds will have no end cities!!!');
                return method;
            }
        },
        // Redirects a field access in the constructor of DedicatedServerProperties from WorldPresets.NORMAL to ASMHooks.overrideDefaultWorldPreset()
        'DedicatedServerPropertiesPatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.server.dedicated.DedicatedServerProperties',
                'methodName': '<init>',
                'methodDesc': '(Ljava/util/Properties;)V'
            },
            'transformer': function (method) {
                var insnList = method.instructions;

                for (var i = 0; i < insnList.size(); ++i) {
                    var insn = insnList.get(i);

                    if (insn.getOpcode() === Opcodes.GETSTATIC && (insn.name.equals('f_226437_') || insn.name.equals('NORMAL'))) {
                        var newInsn = new MethodInsnNode(Opcodes.INVOKESTATIC, 'thedarkcolour/exdeorum/asm/ASMHooks', 'overrideDefaultWorldPreset', '()Lnet/minecraft/resources/ResourceKey;', false)
                        insnList.set(insn, newInsn);

                        ASMAPI.log('INFO', 'Successfully patched server.properties to use void world type by default');
                        return method;
                    }
                }

                ASMAPI.log('ERROR', 'Unable to patch server.properties, you will have to set "level-type" to "exdeorum:void_world" manually.');
                return method;
            }
        },
        // Fixes heightmap issues when placing the end portal podium that would only spawn half of the portal
        // What this patch looks like in code: EndIslandPodium.place(..., this.portalLocation = ASMHooks.prePlaceEndPodium(this.portalLocation))
        'EndPortalPatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.dimension.end.EndDragonFight',
                'methodName': 'spawnExitPortal', // spawnExitPortal
                'methodDesc': '(Z)V'
            },
            'transformer': function(method) {
                var insnList = method.instructions;
                // Cache the mapped method name
                var randomSourceCreate = ASMAPI.mapMethod('m_216327_');

                // Start at 2 to avoid null getPrevious().getPrevious()
                for (var i = 2; i < insnList.size(); ++i) {
                    var insn = insnList.get(i);

					if (insn.getOpcode() == Opcodes.ALOAD && insn.var == 2) {
						// this.portalLocation = ASMHooks.prePlaceEndPodium(this.portalLocation)
						// f_64072_ maps to portalLocation
						insnList.insertBefore(insn, ASMAPI.listOf(
							new VarInsnNode(Opcodes.ALOAD, 0),
							new VarInsnNode(Opcodes.ALOAD, 0),
							new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/world/level/dimension/end/EndDragonFight', 'portalLocation', 'Lnet/minecraft/core/BlockPos;'),
							new MethodInsnNode(Opcodes.INVOKESTATIC, 'thedarkcolour/exdeorum/asm/ASMHooks', 'prePlaceEndPodium', '(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;', false),
							new FieldInsnNode(Opcodes.PUTFIELD, 'net/minecraft/world/level/dimension/end/EndDragonFight', 'portalLocation', 'Lnet/minecraft/core/BlockPos;')
						));
                        ASMAPI.log('INFO', 'Successfully patched end portal.');
						return method;
					}
                }

                ASMAPI.log('ERROR', 'Unable to patch End Portal, it will not spawn properly and you will be unable to return to the overworld without cheats.');
                return method;
            }
        }
    };
}