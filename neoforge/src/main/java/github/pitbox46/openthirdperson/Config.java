package github.pitbox46.openthirdperson;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOCK_CAM = BUILDER
            .comment("Use lock third person cam")
            .define("lock_cam", true);
    public static final ModConfigSpec.DoubleValue CAM_DIST = BUILDER
            .comment("How far away the third person camera should be")
            .defineInRange("cam_dist", 4.0, -64.0, 64.0);
    public static final ModConfigSpec.DoubleValue CAM_SENS = BUILDER
            .comment("Sensitivity of the camera")
            .defineInRange("cam_sens", 1.0, 0, 64.0);

    static final ModConfigSpec SPEC = BUILDER.build();
}
