package github.pitbox46.openthirdperson.client.mixin;

import github.pitbox46.openthirdperson.OpenThirdPerson;
import net.minecraft.client.CameraType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static github.pitbox46.openthirdperson.client.OpenThirdPersonClient.otpCam;

@Mixin(CameraType.class)
public abstract class CameraTypeMixin {
    @Shadow @Final private static CameraType[] VALUES;

    @Inject(method = "cycle", at = @At("RETURN"), cancellable = true)
    public void onCycle(CallbackInfoReturnable<CameraType> cir) {
        if (otpCam.disableRearCamera() && cir.getReturnValue().ordinal() == 2) {
            cir.setReturnValue(CameraType.FIRST_PERSON);
        }
    }
}
