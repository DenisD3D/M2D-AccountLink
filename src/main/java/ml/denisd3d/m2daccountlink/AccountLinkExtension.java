package ml.denisd3d.m2daccountlink;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.Hash;
import ml.denisd3d.minecraft2discord.Minecraft2Discord;
import ml.denisd3d.minecraft2discord.api.M2DExtension;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.ChannelType;
import ml.denisd3d.repack.net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ml.denisd3d.repack.org.apache.commons.collections4.map.PassiveExpiringMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.login.ServerLoginNetHandler;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class AccountLinkExtension extends M2DExtension {
    @Override
    public Boolean onDiscordMessage(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && event.getMessage().getContentRaw().startsWith("!code")) // TODO : Replace with config
        {
            if (M2DAccountLink.codes.containsKey(event.getMessage().getContentRaw().split(" ")[1]))
            {
                M2DAccountLink.discord_ids.put(M2DAccountLink.codes.get(event.getMessage().getContentRaw().split(" ")[1]), event.getAuthor().getIdLong());
                // TODO : return confirm message
            }
            else
            {
                // TODO : return error message
            }
        }

        return true;
    }
}
