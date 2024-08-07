package github.pitbox46.openthirdperson;

import net.minecraft.client.Minecraft;

public class CameraData {
    public double pitch = 0;
    public double yaw = 0;
    public double roll = 0;

    public static boolean isCamDetached() {
        Minecraft mc = Minecraft.getInstance();
        return mc.gameRenderer.getMainCamera().isDetached();
    }

    public static boolean shouldUseLockCam() {
        return isCamDetached() && Config.LOCK_CAM.get();
    }
}
