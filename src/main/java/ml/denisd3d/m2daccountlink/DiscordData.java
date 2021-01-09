package ml.denisd3d.m2daccountlink;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class DiscordData {
    private Long discordId;

    public DiscordData() {
        this(0);
    }

    public DiscordData(long discordId) {
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    // Convert to/from NBT
    public static class DiscordDataNBTStorage implements Capability.IStorage<DiscordData> {
        @Override
        public INBT writeNBT(Capability<DiscordData> capability, DiscordData instance, Direction side) {
            return LongNBT.valueOf(instance.discordId);
        }

        @Override
        public void readNBT(Capability<DiscordData> capability, DiscordData instance, Direction side, INBT nbt) {
            long discordId = 0L;
            if (nbt.getType() == LongNBT.TYPE) {
                discordId = ((LongNBT)nbt).getLong();
            }
            instance.setDiscordId(discordId);
        }
    }

    public static DiscordData createADefaultInstance() {
        return new DiscordData();
    }
}
