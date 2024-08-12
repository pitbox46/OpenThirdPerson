package github.pitbox46.openthirdperson;

import github.pitbox46.openthirdperson.camera.DirectionalFreeCam;
import github.pitbox46.openthirdperson.camera.LockedFreeCam;
import github.pitbox46.openthirdperson.camera.OTPCam;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Supplier;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Cameras> CAMERA = BUILDER
            .comment("Choose which camera to use")
            .defineEnum(List.of("camera"), Cameras.VANILLA);
    public static final ModConfigSpec.DoubleValue CAM_DIST = BUILDER
            .comment("How far away the third person camera should be")
            .defineInRange("cam_dist", 4.0, -64.0, 64.0);
    public static final ModConfigSpec.DoubleValue CAM_SENS = BUILDER
            .comment("Sensitivity of the camera")
            .defineInRange("cam_sens", 1.0, 0, 64.0);

    static final ModConfigSpec SPEC = BUILDER.build();

    public enum Cameras {
        VANILLA(OTPCam::new),
        LOCKED_FREE(LockedFreeCam::new),
        DIRECTIONAL_FREE(DirectionalFreeCam::new);

        public final Supplier<OTPCam> cameraSupplier;
        Cameras(Supplier<OTPCam> cameraSupplier) {
            this.cameraSupplier = cameraSupplier;
        }
    }
}
