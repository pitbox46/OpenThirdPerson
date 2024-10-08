package github.pitbox46.openthirdperson.client.mixin;

import github.pitbox46.openthirdperson.client.camera.OTPCam;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static github.pitbox46.openthirdperson.client.OpenThirdPersonClient.camera;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void onTurn(LocalPlayer instance, double yRot, double xRot) {
        if (OTPCam.isCamDetached()) {
            camera.handleMouseMovement(instance, yRot, xRot);
        } else {
            instance.turn(yRot, xRot);
        }
    }
}
