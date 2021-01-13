package ml.denisd3d.m2daccountlink;

import ml.denisd3d.m2daccountlink.storage.DiscordStorageHolder;
import ml.denisd3d.minecraft2discord.Minecraft2Discord;
import ml.denisd3d.minecraft2discord.api.M2DExtension;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.ChannelType;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.Guild;
import ml.denisd3d.repack.net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AccountLinkExtension extends M2DExtension {
    @Override
    public Boolean onDiscordMessage(MessageReceivedEvent event) {

        if (!event.getAuthor().isBot() && event.isFromType(ChannelType.PRIVATE) && event.getMessage().getContentRaw().startsWith(AccountLinkConfig.SERVER.linkCommand.get())) {
            String code = event.getMessage().getContentRaw().substring(AccountLinkConfig.SERVER.linkCommand.get().length());
            if (M2DAccountLink.codes.containsKey(code)) {
                if (AccountLinkConfig.SERVER.rolesCheckEnabled.get()) {
                    Guild guild = Minecraft2Discord.getDiscordBot().getGuildById(AccountLinkConfig.SERVER.guildId.get());
                    if (guild != null) {
                        guild.retrieveMemberById(event.getAuthor().getIdLong()).queue(member -> {
                            if (member.getRoles().stream().anyMatch(role -> AccountLinkConfig.SERVER.requiredAnyRolesIdsOf.get().contains(role.getIdLong()))) {
                                DiscordStorageHolder.addDiscordId(M2DAccountLink.codes.get(code), event.getAuthor().getIdLong());
                                M2DAccountLink.codes.remove(code);
                                event.getChannel().sendMessage(AccountLinkConfig.SERVER.linkSuccess.get()).queue();
                            } else {
                                event.getChannel().sendMessage(AccountLinkConfig.SERVER.missingRolesKickMessage.get()).queue();
                            }
                        });
                    }
                } else {
                    DiscordStorageHolder.addDiscordId(M2DAccountLink.codes.get(code), event.getAuthor().getIdLong());
                    M2DAccountLink.codes.remove(code);
                    event.getChannel().sendMessage(AccountLinkConfig.SERVER.linkSuccess.get()).queue();
                }
            } else {
                event.getChannel().sendMessage(AccountLinkConfig.SERVER.invalidLinkCode.get()).queue();
            }
        }

        return true;
    }
}
