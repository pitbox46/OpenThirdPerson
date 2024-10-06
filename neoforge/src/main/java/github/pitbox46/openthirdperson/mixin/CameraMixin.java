package github.pitbox46.openthirdperson.mixin;

import github.pitbox46.openthirdperson.OpenThirdPerson;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void move(float zoom, float dy, float dx);

    @Shadow private Vec3 position;

    @Shadow @Final private Vector3f forwards;

    @Shadow private BlockGetter level;

    @Shadow private Entity entity;

    @Redirect(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;move(FFF)V", ordinal = 0))
    private void setOffset(Camera instance, float zoom, float dy, float dx) {
        Vector3f offset = OpenThirdPerson.camera.computeOffset(instance, new Vector3f(dx, dy, zoom));
        this.move(offset.z, offset.y, offset.x);
    }

    @Unique
    private float openThirdPerson$getMaxZoom(Vector3f maxOffset) {
        float f = 0.1F;
        float maxZoom = maxOffset.length();



        for (int i = 0; i < 8; i++) {
            float f1 = (float)((i & 1) * 2 - 1);
            float f2 = (float)((i >> 1 & 1) * 2 - 1);
            float f3 = (float)((i >> 2 & 1) * 2 - 1);
            Vec3 vec3 = this.position.add(f1 * f, f2 * f, f3 * f);
            Vec3 vec31 = vec3.add(new Vec3(this.forwards).add(new Vec3(maxOffset)).normalize().scale(-maxZoom));
            HitResult hitresult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, this.entity));
            if (hitresult.getType() != HitResult.Type.MISS) {
                float f4 = (float)hitresult.getLocation().distanceToSqr(this.position);
                if (f4 < Mth.square(maxZoom)) {
                    maxZoom = Mth.sqrt(f4);
                }
            }
        }

        return maxZoom;
    }
}
