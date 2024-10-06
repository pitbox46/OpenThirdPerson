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
     * x, y, z offset from normal camera position
     */
    protected Vector3f offset = new Vector3f(0, 0, 0);

    /**
     * Disables the rear facing third person option.
     * It saves the user from having to hit F5 twice to get to first person.
     * @return if the backwards facing third person option should be disabled
     */
    public boolean disableRearCamera() {
        return false;
    }

    /**
     * @param camera      minecraft camera
     * @param pAngles     normal yaw, pitch, roll
     * @param partialTick
     * @return new yaw, pitch, roll
     */
    public Vector3f computeAngles(Camera camera, Vector3f pAngles, float partialTick) {
        this.angles = pAngles;
        return pAngles;
    }

    /**
     *
     * @param camera - minecraft camera
     * @param offset - camera offset from the player
     * @return New offset. Applied after rotations are applied. Calculations will be applied to this value so it doesn't clip beyond blocks
     */
    public Vector3f computeOffset(Camera camera, Vector3f offset) {
        this.offset = offset.setComponent(0, computeDist(camera, offset.x));
        return  offset;
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

    /**
     * Called at the beginning of client tick
     */
    public void tick() {}

    public static boolean isCamDetached() {
        Minecraft mc = Minecraft.getInstance();
        return mc.gameRenderer.getMainCamera().isDetached();
    }
}
