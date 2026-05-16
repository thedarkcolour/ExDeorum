package thedarkcolour.exdeorum.coremod;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Set;

// Redirects a field access in the constructor of DedicatedServerProperties from WorldPresets.NORMAL to ASMHooks.overrideDefaultWorldPreset()
public class DedicatedServerPropertiesTransformer extends SimpleMethodProcessor {
    private static final ProcessorName NAME = new ProcessorName("exdeorum", "dedicated_server_properties_transformer");

    @Override
    public ProcessorName name() {
        return NAME;
    }

    @Override
    public Set<Target> targets() {
        return Set.of(new Target(
                "net.minecraft.server.dedicated.DedicatedServerProperties",
                "<init>",
                "(Ljava/util/Properties;)V"
        ));
    }

    @Override
    public void transform(MethodNode input, SimpleTransformationContext context) {
        var insnList = input.instructions;

        for (var i = 0; i < insnList.size(); ++i) {
            var insn = insnList.get(i);

            if (insn.getOpcode() == Opcodes.GETSTATIC && (((FieldInsnNode) insn).name.equals("f_226437_") || ((FieldInsnNode) insn).name.equals("NORMAL"))) {
                var newInsn = new MethodInsnNode(Opcodes.INVOKESTATIC, "thedarkcolour/exdeorum/asm/ASMHooks", "overrideDefaultWorldPreset", "()Lnet/minecraft/resources/ResourceKey;", false);
                insnList.set(insn, newInsn);

                ExDeorumASM.LOGGER.info("Successfully patched server.properties to use void world type by default");
                return;
            }
        }

        ExDeorumASM.LOGGER.error("Unable to patch server.properties, you will have to set \"level-type\" to \"exdeorum:void_world\" manually.");
    }
}
