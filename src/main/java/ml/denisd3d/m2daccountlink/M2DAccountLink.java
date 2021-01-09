package ml.denisd3d.m2daccountlink;

import ml.denisd3d.minecraft2discord.api.M2DUtils;
import ml.denisd3d.repack.org.apache.commons.collections4.map.PassiveExpiringMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("m2d-account-link")
public class M2DAccountLink {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<String, UUID>
            expirationPolicy = new PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<>(
            10, TimeUnit.MINUTES);
    public static PassiveExpiringMap<String, UUID> codes = new PassiveExpiringMap<>(expirationPolicy);

    public static HashMap<UUID, Long> discord_ids = new HashMap<>();
    public static Random random = new Random();

    public M2DAccountLink() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(CapabilityAttachEventHandler.class);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        CapabilityDiscordData.register();
        M2DUtils.registerExtension(new AccountLinkExtension());
    }
}
