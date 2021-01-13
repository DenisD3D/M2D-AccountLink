package ml.denisd3d.m2daccountlink;

import com.mojang.authlib.GameProfile;
import ml.denisd3d.m2daccountlink.storage.DiscordStorageHolder;
import ml.denisd3d.minecraft2discord.Minecraft2Discord;
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
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("m2d-account-link")
public class M2DAccountLink {

    public static final Logger LOGGER = LogManager.getLogger();

    public static PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<String, GameProfile>
            expirationPolicy = new PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<>(
            10, TimeUnit.MINUTES);
    public static PassiveExpiringMap<String, GameProfile> codes = new PassiveExpiringMap<>(expirationPolicy);

    public static Random random = new Random();

    public M2DAccountLink() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AccountLinkConfig.SERVER_SPECS, "m2d-account-link.toml");
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        M2DUtils.registerExtension(new AccountLinkExtension());
    }

    @SubscribeEvent
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        DiscordStorageHolder.loadDiscordIdsList();
        DiscordStorageHolder.saveDiscordIdsList();
    }
}
