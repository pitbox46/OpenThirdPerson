package github.pitbox46.openthirdperson.camera;

import github.pitbox46.openthirdperson.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector3f;

public class CardinalCam extends OTPCam {
    protected float prevRot = 0;

    @Override
    public boolean disableRearCamera() {
        return true;
    }

    @Override
    public Vector3f computeAngles(Camera camera, Vector3f pAngles) {
        return this.angles;
    }

    @Override
    public void handleMouseMovement(LocalPlayer player, double yRot, double xRot) {
        angles.add(
                (float) (yRot * 0.13 * Config.CAM_SENS.get()),
                (float) (xRot * 0.13 * Config.CAM_SENS.get()),
                0
        );
    }

    @Override
    public void handlePlayerMovement(Input input) {
        LocalPlayer player = Minecraft.getInstance().player;
        Vec2 movement = input.getMoveVector();
        if (movement.lengthSquared() > 0) {
            float deg = (float) Mth.atan2(movement.x, movement.y) * -Mth.RAD_TO_DEG;
            player.setYRot(prevRot + deg);
            input.up = false;
            input.down = false;
            input.right = false;
            input.left = false;
            input.forwardImpulse = 1;
            input.leftImpulse = 0;
        } else {
            prevRot = player.getYRot();
        }
    }
}