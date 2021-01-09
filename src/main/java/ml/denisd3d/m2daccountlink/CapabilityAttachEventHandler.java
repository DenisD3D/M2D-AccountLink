package ml.denisd3d.m2daccountlink;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityAttachEventHandler {

    @SubscribeEvent
    public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation("m2d-account-link:capability_discord_data") , new CapabilityProviderEntities());
        }
    }
}