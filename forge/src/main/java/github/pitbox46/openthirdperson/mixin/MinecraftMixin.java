package github.pitbox46.openthirdperson.mixin;

import github.pitbox46.openthirdperson.camera.OTPCam;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static github.pitbox46.openthirdperson.OpenThirdPerson.camera;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "startAttack", at = @At("HEAD"))
    private void onAttack(CallbackInfoReturnable<Boolean> cir) {
        if (OTPCam.isCamDetached()) {
            camera.handleInteraction();
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void onUseItem(CallbackInfo ci) {
        if (OTPCam.isCamDetached()) {
            camera.handleInteraction();
        }
    }
}