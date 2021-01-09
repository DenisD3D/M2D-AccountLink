package ml.denisd3d.m2daccountlink;

import ml.denisd3d.minecraft2discord.api.M2DExtension;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.ChannelType;
import ml.denisd3d.repack.net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AccountLinkExtension extends M2DExtension {
    @Override
    public Boolean onDiscordMessage(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && event.getMessage().getContentRaw().startsWith("!code")) // TODO : Replace with config
        {
            // TODO : Validate thing
        }

        return true;
    }
}
