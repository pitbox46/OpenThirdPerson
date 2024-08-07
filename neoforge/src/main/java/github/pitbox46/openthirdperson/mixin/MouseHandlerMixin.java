package github.pitbox46.openthirdperson.mixin;

import github.pitbox46.openthirdperson.CameraData;
import github.pitbox46.openthirdperson.Config;
import github.pitbox46.openthirdperson.OpenThirdPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow @Final private Minecraft minecraft;

    @Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void onTurn(LocalPlayer instance, double yRot, double xRot) {
        if (CameraData.shouldUseLockCam() && OpenThirdPerson.MOVE_CAM.get().isDown()) {
            OpenThirdPerson.CAMERA_DATA.pitch += xRot * 0.13 * Config.CAM_SENS.get();
            OpenThirdPerson.CAMERA_DATA.yaw += yRot * 0.13 * Config.CAM_SENS.get();
        } else {
            minecraft.player.turn(yRot, xRot);
        }
    }
}
