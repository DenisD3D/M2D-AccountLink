package ml.denisd3d.m2daccountlink;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityDiscordData {
    @CapabilityInject(DiscordData.class)
    public static Capability<DiscordData> CAPABILITY_DISCORD_DATA = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(
                DiscordData.class,
                new DiscordData.DiscordDataNBTStorage(),
                DiscordData::createADefaultInstance);
    }
}
