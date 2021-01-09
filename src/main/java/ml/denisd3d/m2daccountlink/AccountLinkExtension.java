package ml.denisd3d.m2daccountlink;

import ml.denisd3d.minecraft2discord.api.M2DExtension;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.ChannelType;
import ml.denisd3d.repack.net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AccountLinkExtension extends M2DExtension {
    @Override
    public Boolean onDiscordMessage(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && event.getMessage().getContentRaw().startsWith("!code")) // TODO : Replace with config
        {
            if (M2DAccountLink.codes.containsKey(event.getMessage().getContentRaw().split(" ")[1]))
            {
                M2DAccountLink.discord_ids.put(M2DAccountLink.codes.get(event.getMessage().getContentRaw().split(" ")[1]), event.getAuthor().getIdLong());
                M2DAccountLink.codes.remove(event.getMessage().getContentRaw().split(" ")[1]);
                event.getChannel().sendMessage("You now have access to the server").queue(); // TODO : Replace with config
            }
            else
            {
                event.getChannel().sendMessage("Your code is invalid. Please try again").queue(); // TODO : Replace with config
            }
        }

        return true;
    }

    @Override
    public Boolean onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        AtomicInteger return_value = new AtomicInteger(0);
        event.getPlayer().getCapability(CapabilityDiscordData.CAPABILITY_DISCORD_DATA).ifPresent(discordData -> {
            if (discordData.getDiscordId() != 0)
            {
                return_value.set(1);
            }
            else if (M2DAccountLink.discord_ids.containsKey(event.getPlayer().getUniqueID()))
            {
                discordData.setDiscordId(M2DAccountLink.discord_ids.get(event.getPlayer().getUniqueID()));
                return_value.set(1);
            }
            else
            {
                String code = String.format("%04d", M2DAccountLink.random.nextInt(10000));
                M2DAccountLink.codes.put(code, event.getPlayer().getUniqueID());
                ((ServerPlayerEntity)event.getPlayer()).connection.disconnect(new StringTextComponent("Send to <the bot> a message with : !code " + code)); // TODO : replace with config
                return_value.set(0);
            }
        });
        return return_value.get() == 1 ? true : null;
    }

    @Override
    public Boolean onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (M2DAccountLink.codes.containsValue(event.getPlayer().getUniqueID()))
        {
            return null;
        }
        else
        {
            return true;
        }
    }
}
