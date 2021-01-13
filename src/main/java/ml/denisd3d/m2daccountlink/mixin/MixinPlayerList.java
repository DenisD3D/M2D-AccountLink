package ml.denisd3d.m2daccountlink.mixin;

import com.mojang.authlib.GameProfile;
import ml.denisd3d.m2daccountlink.AccountLinkConfig;
import ml.denisd3d.m2daccountlink.M2DAccountLink;
import ml.denisd3d.m2daccountlink.storage.DiscordStorageHolder;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;
import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {

    @Inject(method = "canPlayerLogin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/util/text/ITextComponent;", at = @At(value = "HEAD"), cancellable = true)
    public void initializeConnectionToPlayer(SocketAddress socketAddress, GameProfile gameProfile, CallbackInfoReturnable<ITextComponent> cir) {
        if (!DiscordStorageHolder.getDiscordIds().hasEntry(gameProfile) && !M2DAccountLink.codes.containsValue(gameProfile)) {
            String code = String.format("%04d", M2DAccountLink.random.nextInt(10000));
            M2DAccountLink.codes.put(code, gameProfile);
            cir.setReturnValue(new StringTextComponent(AccountLinkConfig.SERVER.kickMessage.get() + code));
        } else if (!DiscordStorageHolder.getDiscordIds().hasEntry(gameProfile) && M2DAccountLink.codes.containsValue(gameProfile)) {
            String code = null;
            try {
                code = M2DAccountLink.codes.entrySet().stream().filter(gameProfileEntry -> gameProfileEntry.getValue().getId().equals(gameProfile.getId())).findFirst().orElseThrow(() -> new Exception("Discord link code not found")).getKey();
            } catch (Exception e) {
                e.printStackTrace();
                code = "[Error please contact server admin]";
            }
            cir.setReturnValue(new StringTextComponent(AccountLinkConfig.SERVER.kickMessage.get() + code));
        }
    }
}

