package github.pitbox46.openthirdperson.camera;

import github.pitbox46.openthirdperson.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DirectionalFreeCam extends OTPCam {
    @Override
    public boolean disableRearCamera() {
        return true;
    }

    @Override
    public Vector3f computeAngles(Camera camera, Vector3f pAngles) {
        return super.computeAngles(camera, pAngles);
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
    public void handlePlayerMovement(LocalPlayer player) {
        Vec3 mov = player.getDeltaMovement();

    }
}
