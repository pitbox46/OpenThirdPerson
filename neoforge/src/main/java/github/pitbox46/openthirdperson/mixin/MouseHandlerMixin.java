package github.pitbox46.openthirdperson.mixin;

import github.pitbox46.openthirdperson.OpenThirdPerson;
import github.pitbox46.openthirdperson.camera.OTPCam;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void onTurn(LocalPlayer instance, double yRot, double xRot) {
        if (OTPCam.isCamDetached()) {
            OpenThirdPerson.currentOTPCam.handleMouseMovement(instance, yRot, xRot);
        } else {
            instance.turn(yRot, xRot);
        }
    }
}
