package github.pitbox46.openthirdperson.client.mixin;

import github.pitbox46.openthirdperson.client.camera.OTPCam;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static github.pitbox46.openthirdperson.client.OpenThirdPersonClient.normalOTPCam;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow public Input input;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V"))
    private void onMovementInputUpdate(CallbackInfo ci) {
        if (OTPCam.isCamDetached()) {
            normalOTPCam.handlePlayerMovement(input);
        }
    }
}
