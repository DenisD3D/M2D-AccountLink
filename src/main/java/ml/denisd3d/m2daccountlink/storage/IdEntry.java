package ml.denisd3d.m2daccountlink.storage;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.management.UserListEntry;

import java.util.UUID;

public class IdEntry extends UserListEntry<GameProfile> {
    private final long id;

    public IdEntry(GameProfile player, long id) {
        super(player);
        this.id = id;
    }

    public IdEntry(JsonObject jsonObject) {
        super(constructProfile(jsonObject));
        this.id = jsonObject.has("id") ? jsonObject.get("id").getAsLong() : 0;
    }

    private static GameProfile constructProfile(JsonObject jsonObject) {
        if (jsonObject.has("uuid") && jsonObject.has("name")) {
            String s = jsonObject.get("uuid").getAsString();

            UUID uuid;
            try {
                uuid = UUID.fromString(s);
            } catch (Throwable throwable) {
                return null;
            }

            return new GameProfile(uuid, jsonObject.get("name").getAsString());
        } else {
            return null;
        }
    }

    public long getId() {
        return id;
    }

    @Override
    protected void onSerialization(JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", this.getValue().getId() == null ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            data.addProperty("id", this.id);
        }
    }
}
