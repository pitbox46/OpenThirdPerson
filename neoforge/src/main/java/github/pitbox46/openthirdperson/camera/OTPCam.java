package github.pitbox46.openthirdperson.camera;

import github.pitbox46.openthirdperson.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector3f;

/**
 * A normal Minecraft 3rd person camera
 */
public class OTPCam {
    /**
     * yaw, pitch, roll in degrees
     */
    protected Vector3f angles = new Vector3f(0, 0, 0);

    /**
     * Disables the rear facing third person option.
     * It saves the user from having to hit F5 twice to get to first person.
     * @return if the backwards facing third person option should be disabled
     */
    public boolean disableRearCamera() {
        //TODO This
        return false;
    }

    /**
     * @param camera minecraft camera
     * @param pAngles normal yaw, pitch, roll
     * @return new yaw, pitch, roll
     */
    public Vector3f computeAngles(Camera camera, Vector3f pAngles) {
        this.angles = pAngles;
        return pAngles;
    }

    /**
     * @param camera minecraft camera
     * @param pDist normal distance
     * @return new distance
     */
    public float computeDist(Camera camera, float pDist) {
        return Config.CAM_DIST.get().floatValue();
    }

    /**
     * Handles the player (or camera) turning due to mouse movements
     * @param player
     * @param yRot
     * @param xRot
     */
    public void handleMouseMovement(LocalPlayer player, double yRot, double xRot) {
        player.turn(yRot, xRot);
    }

    /**
     * Handles player movement inputs
     */
    public void handlePlayerMovement(Input input) {
    }

    /**
     * Handles left clicks
     */
    public void handleInteraction() {
    }

    public static boolean isCamDetached() {
        Minecraft mc = Minecraft.getInstance();
        return mc.gameRenderer.getMainCamera().isDetached();
    }
}
