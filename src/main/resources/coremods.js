// Type definitions

var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

function initializeCoreMod() {
    return {
        // inserts a hook into EndCityStructure#findGenerationPoint to fix the position of the city if it is in a void world
        'EndCityPatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.levelgen.structure.structures.EndCityStructure',
                'methodName': 'm_214086_',
                'methodDesc': '(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/util/Optional;'
            },
            'transformer': function (method) {
                var insnList = method.instructions;

                for (var i = 0; i < insnList.size(); ++i) {
                    var insn = insnList.get(i);

                    // patch before ASTORE 3
                    if (insn.getOpcode() == Opcodes.ASTORE && insn.var == 3) {
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
        // Redirects a field access in the constructor of DedicatedServerProperties from WorldPresets.NORMAL to EWorldPresets.VOID_WORLD
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
                        insn.owner = 'thedarkcolour/exdeorum/registry/EWorldPresets';
                        insn.name = 'VOID_WORLD';

                        ASMAPI.log('INFO', 'Successfully patched server.properties to use void world type by default');
                        return method;
                    }
                }

                ASMAPI.log('ERROR', 'Unable to patch server.properties, you will have to set "level-type" to "exdeorum:void_world" manually.');
                return method;
            }
        }
    };
}