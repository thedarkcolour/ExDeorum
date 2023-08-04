package thedarkcolour.exdeorum.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Server SERVER;
    public static final Client CLIENT;

    public static class Server {
        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server configuration for Ex Nihilo Reborn")
                    .push("server");
        }
    }

    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {

        }
    }

    static {
        {
            Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
        {
            Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
    }
}
