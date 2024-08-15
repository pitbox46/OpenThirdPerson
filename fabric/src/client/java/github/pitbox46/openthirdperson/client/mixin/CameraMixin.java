package github.pitbox46.openthirdperson.client.mixin;

import github.pitbox46.openthirdperson.client.camera.OTPCam;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static github.pitbox46.openthirdperson.client.OpenThirdPersonClient.otpCam;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private float yRot;
    @Shadow private float xRot;

    @Shadow @Deprecated protected abstract void setRotation(float yRot, float xRot);

    @Inject(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V"))
    private void setRotation(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        if (OTPCam.isCamDetached()) {
            Vector3f angles = otpCam.computeAngles(
                    (Camera) (Object) this,
                    new Vector3f(this.yRot, this.xRot, 0)
            );
            setRotation(angles.x, angles.y);
        }
    }

    @ModifyVariable(method = "getMaxZoom", argsOnly = true, at = @At(value = "LOAD", ordinal = 0))
    private float modifyMaxZoom(float zoom) {
        return otpCam.computeDist((Camera) (Object) this, zoom);
    }
}
