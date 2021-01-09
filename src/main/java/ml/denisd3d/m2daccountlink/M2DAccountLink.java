package ml.denisd3d.m2daccountlink;

import ml.denisd3d.minecraft2discord.api.M2DUtils;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("m2d-account-link")
public class M2DAccountLink {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public M2DAccountLink() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        M2DUtils.registerExtension(new AccountLinkExtension());
    }
}
