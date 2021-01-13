package ml.denisd3d.m2daccountlink.storage;

import com.mojang.authlib.GameProfile;
import ml.denisd3d.m2daccountlink.M2DAccountLink;
import net.minecraft.server.management.BanList;

import java.io.File;

public class DiscordStorageHolder {
    public static final File FILE_DISCORD_IDS = new File("discord-ids.json");
    private static final IdsList discordIds = new IdsList(FILE_DISCORD_IDS);

    public static IdsList getDiscordIds() {
        return discordIds;
    }

    public static void loadDiscordIdsList() {
        try {
            getDiscordIds().readSavedFile();
        } catch (Exception exception) {
            M2DAccountLink.LOGGER.warn("Failed to load discord ids list: ", (Throwable)exception);
        }
    }

    public static void saveDiscordIdsList() {
        try {
            getDiscordIds().writeChanges();
        } catch (Exception exception) {
            M2DAccountLink.LOGGER.warn("Failed to save discord ids list: ", (Throwable)exception);
        }
    }

    public static void addDiscordId(GameProfile profile, long discordId)
    {
        getDiscordIds().addEntry(new IdEntry(profile, discordId));
        saveDiscordIdsList();
    }

    public static void removeDiscordId(GameProfile profile, long discordId)
    {
        getDiscordIds().removeEntry(profile);
        saveDiscordIdsList();
    }
}
