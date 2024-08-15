package github.pitbox46.openthirdperson.client.camera;

import github.pitbox46.openthirdperson.client.Config;
import github.pitbox46.openthirdperson.client.OpenThirdPersonClient;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector3f;

/**
 * Allows the user to move the camera around freely when they're pressing a button (middlemouse default)
 */
public class LockedFreeCam extends OTPCam {
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
        if (isUnlocked()) {
            angles.add(
                    (float) (yRot * 0.13 * Config.CAM_SENS.get()),
                    (float) (xRot * 0.13 * Config.CAM_SENS.get()),
                    0
            );
        } else {
            super.handleMouseMovement(player, yRot, xRot);
        }
    }

    public boolean isUnlocked() {
        return OpenThirdPersonClient.CAM_BUTTON.isDown();
    }
}
