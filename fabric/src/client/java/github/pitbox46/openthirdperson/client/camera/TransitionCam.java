package github.pitbox46.openthirdperson.client.camera;

import github.pitbox46.openthirdperson.client.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

/**
 * A wrapper that transitions between two different cameras
 */
public class TransitionCam extends OTPCam {
    /**
     * Time to transition in ticks
     */
    public static final float TRANS_TIME = 5;

    public OTPCam startCam;
    public OTPCam finishCam;

    Vector3f startAngles = null;
    float startDist = Float.MIN_VALUE;
    int tick = 0;

    public TransitionCam(OTPCam start) {
        startCam = start;
        finishCam = start;
    }

    public void changeCamera(OTPCam newCamera) {
        if (newCamera == finishCam) {
            return;
        }
        startCam = finishCam;
        finishCam = newCamera;
        tick = 0;
        startAngles = null;
        startDist = Float.MIN_VALUE;
    }

    public void tick() {
        tick += 1;
    }

    @Override
    public boolean disableRearCamera() {
        return finishCam.disableRearCamera();
    }

    @Override
    public void handleInteraction() {
        finishCam.handleInteraction();
    }

    @Override
    public void handlePlayerMovement(Input input) {
        finishCam.handlePlayerMovement(input);
    }

    @Override
    public void handleMouseMovement(LocalPlayer player, double yRot, double xRot) {
        finishCam.handleMouseMovement(player, yRot, xRot);
    }

    @Override
    public float computeDist(Camera camera, float pDist) {
        if (Config.SMOOTH_TRANSITION.get() && tick < TRANS_TIME) {
            if (startDist == Float.MIN_VALUE) {
                startDist = startCam.computeDist(camera, pDist);
            }
            return Mth.lerp(getDelta(0), startCam.computeDist(camera, pDist), finishCam.computeDist(camera, pDist));
        }
        return finishCam.computeDist(camera, pDist);
    }

    @Override
    public Vector3f computeAngles(Camera camera, Vector3f pAngles, float partialTick) {
        if (Config.SMOOTH_TRANSITION.get() && tick < TRANS_TIME) {
            float delta = getDelta(partialTick);
            if (startAngles == null) {
                startAngles = startCam.computeAngles(camera, pAngles, partialTick);
            }
            Vector3f finishAngles = finishCam.computeAngles(camera, pAngles, 0);
            return new Vector3f(
                    Mth.rotLerp(delta, startAngles.x, finishAngles.x),
                    Mth.rotLerp(delta, startAngles.y, finishAngles.y),
                    Mth.rotLerp(delta, startAngles.z, finishAngles.z)
            );
        }
        return finishCam.computeAngles(camera, pAngles, partialTick);
    }

    public boolean shouldTransition() {
        return Config.SMOOTH_TRANSITION.get() && startCam != finishCam && tick < TRANS_TIME;
    }

    public float getDelta(float partialTick) {
        return (tick + partialTick) / TRANS_TIME;
    }
}
