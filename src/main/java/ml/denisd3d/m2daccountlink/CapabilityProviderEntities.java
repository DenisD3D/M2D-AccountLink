package ml.denisd3d.m2daccountlink;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderEntities implements ICapabilitySerializable<INBT> {

    private final Direction NO_SPECIFIC_SIDE = null;

    private DiscordData discordData = new DiscordData();
    private static final Logger LOGGER = LogManager.getLogger();

    private final static String DISCORD_NBT = "discord";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (CapabilityDiscordData.CAPABILITY_DISCORD_DATA == capability) {
            return (LazyOptional<T>)LazyOptional.of(()-> discordData);
        }

        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        INBT discordNBT = CapabilityDiscordData.CAPABILITY_DISCORD_DATA.writeNBT(discordData, NO_SPECIFIC_SIDE);
        nbt.put(DISCORD_NBT, discordNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT)nbt;
        INBT discordNBT = compoundNBT.get(DISCORD_NBT);

        CapabilityDiscordData.CAPABILITY_DISCORD_DATA.readNBT(discordData, NO_SPECIFIC_SIDE, discordNBT);
    }

}