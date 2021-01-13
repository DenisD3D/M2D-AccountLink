package ml.denisd3d.m2daccountlink.storage;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.management.ProfileBanEntry;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;

import java.io.File;

public class IdsList extends UserList<GameProfile, IdEntry> {
    public IdsList(File saveFile) {
        super(saveFile);
    }

    protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
        return new IdEntry(entryData);
    }

    protected String getObjectKey(GameProfile obj) {
        return obj.getId().toString();
    }
}
