package github.pitbox46.openthirdperson.client.mixin;

import net.minecraft.client.CameraType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static github.pitbox46.openthirdperson.client.OpenThirdPersonClient.normalOTPCam;

@Mixin(CameraType.class)
public abstract class CameraTypeMixin {
    @Inject(method = "cycle", at = @At("RETURN"), cancellable = true)
    public void onCycle(CallbackInfoReturnable<CameraType> cir) {
        if (normalOTPCam.disableRearCamera() && cir.getReturnValue().ordinal() == 2) {
            cir.setReturnValue(CameraType.FIRST_PERSON);
        }
    }
}
