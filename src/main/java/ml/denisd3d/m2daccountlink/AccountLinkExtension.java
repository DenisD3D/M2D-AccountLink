package ml.denisd3d.m2daccountlink;

import ml.denisd3d.minecraft2discord.Config;
import ml.denisd3d.minecraft2discord.Minecraft2Discord;
import ml.denisd3d.minecraft2discord.api.M2DExtension;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.ChannelType;
import ml.denisd3d.repack.net.dv8tion.jda.api.entities.Guild;
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
        if (event.isFromType(ChannelType.PRIVATE) && event.getMessage().getContentRaw().startsWith(AccountLinkConfig.SERVER.linkCommand.get()))
        {
            String code = (event.getMessage().getContentRaw() + " .").split(" ")[1]; // Add a space to the message so we can not be out of bound
            if (M2DAccountLink.codes.containsKey(code))
            {
                if (AccountLinkConfig.SERVER.rolesCheckEnabled.get())
                {
                    Guild guild = Minecraft2Discord.getDiscordBot().getGuildById(AccountLinkConfig.SERVER.guildId.get());
                    if (guild != null) {
                        guild.retrieveMemberById(event.getAuthor().getIdLong()).queue(member -> {
                            if (member.getRoles().stream().anyMatch(role -> AccountLinkConfig.SERVER.requiredAnyRolesIdsOf.get().contains(role.getIdLong()))) {
                                M2DAccountLink.discord_ids.put(M2DAccountLink.codes.get(code), event.getAuthor().getIdLong());
                                M2DAccountLink.codes.values().removeIf(uuid -> uuid.equals(M2DAccountLink.codes.get(code)));
                                event.getChannel().sendMessage(AccountLinkConfig.SERVER.linkSuccess.get()).queue();
                            } else
                            {
                                event.getChannel().sendMessage(AccountLinkConfig.SERVER.missingRolesKickMessage.get()).queue();
                            }
                        });
                    }
                }
                else
                {
                    M2DAccountLink.discord_ids.put(M2DAccountLink.codes.get(code), event.getAuthor().getIdLong());
                    M2DAccountLink.codes.remove(code);

                    event.getChannel().sendMessage(AccountLinkConfig.SERVER.linkSuccess.get()).queue();
                }

            }
            else
            {
                event.getChannel().sendMessage(AccountLinkConfig.SERVER.invalidLinkCode.get()).queue();
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
                M2DAccountLink.discord_ids.remove(event.getPlayer().getUniqueID());
                return_value.set(1);
            }
            else
            {
                String code = String.format("%04d", M2DAccountLink.random.nextInt(10000));
                M2DAccountLink.codes.put(code, event.getPlayer().getUniqueID());
                ((ServerPlayerEntity)event.getPlayer()).connection.disconnect(new StringTextComponent(AccountLinkConfig.SERVER.kickMessage.get() + code));
                return_value.set(0);
            }

            if (return_value.get() == 1 && AccountLinkConfig.SERVER.rolesCheckEnabled.get())
            {
                Guild guild = Minecraft2Discord.getDiscordBot().getGuildById(AccountLinkConfig.SERVER.guildId.get());
                if (guild != null)
                {
                    guild.retrieveMemberById(discordData.getDiscordId()).queue(member -> {
                    if (member.getRoles().stream().noneMatch(role -> AccountLinkConfig.SERVER.requiredAnyRolesIdsOf.get().contains(role.getIdLong())))
                    {
                        if (discordData.getDiscordId() != 0)
                        {
                            discordData.setDiscordId(0);
                        }
                        ((ServerPlayerEntity)event.getPlayer()).connection.disconnect(new StringTextComponent(AccountLinkConfig.SERVER.missingRolesKickMessage.get()));
                    }
                });
                }
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
