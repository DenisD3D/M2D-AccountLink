package ml.denisd3d.m2daccountlink;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountLinkConfig {
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPECS;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPECS = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class ServerConfig {

        public final ForgeConfigSpec.ConfigValue<String> linkCommand;

        public final ForgeConfigSpec.ConfigValue<String> kickMessage;
        public final ForgeConfigSpec.ConfigValue<String> invalidLinkCode;
        public final ForgeConfigSpec.ConfigValue<String> linkSuccess;

        public final ForgeConfigSpec.BooleanValue rolesCheckEnabled;
        public final ForgeConfigSpec.ConfigValue<List<? extends Long>> requiredAnyRolesIdsOf;
        public final ForgeConfigSpec.LongValue guildId;
        public final ForgeConfigSpec.ConfigValue<String> missingRolesKickMessage;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.comment(" Configuration file for M2D Account Link extension for Minecraft2Discord\n" +
                    "   - Curseforge : https://www.curseforge.com/minecraft/mc-mods/m2d-account-link\n" +
                    "   - Github : https://github.com/Denis3D/M2D-AccountLink\n" +
                    "   - Discord : https://discord.gg/rzzd76c").push("M2DAccountLink");


            linkCommand = builder.comment(" Set an empty string to just send the code. Don't forget the space at the end if you want the command and the code separated").define("linkCommand", "!code ");

            kickMessage = builder.define("kickMessage", "Send to <YOUR BOT> a message with : !code ");
            invalidLinkCode = builder.define("invalidLinkCode", "Your code is invalid. Please try again");
            linkSuccess = builder.define("linkSuccess", "You now have access to the server");

            builder.push("Roles");
            rolesCheckEnabled = builder.define("rolesCheckEnabled", false);
            requiredAnyRolesIdsOf = builder.defineList("requiredAnyRolesIdsOf", ArrayList::new, e -> e instanceof Long);
            guildId = builder.defineInRange("guildId", 0, 0, Long.MAX_VALUE);
            missingRolesKickMessage = builder.define("missingRolesKickMessage", "You are missing a role to join the server");
            builder.pop();

            builder.pop();
        }

    }

}
