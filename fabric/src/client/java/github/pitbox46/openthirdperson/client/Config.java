package github.pitbox46.openthirdperson.client;

import github.pitbox46.openthirdperson.client.camera.CardinalCam;
import github.pitbox46.openthirdperson.client.camera.LockedFreeCam;
import github.pitbox46.openthirdperson.client.camera.OTPCam;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.function.Supplier;

@me.shedaniel.autoconfig.annotation.Config(name = OpenThirdPersonClient.MODID)
public class Config implements ConfigData {
    @ConfigEntry.Category("General")
    @ConfigEntry.Gui.Tooltip
    public Cameras camera = Cameras.VANILLA;
    @ConfigEntry.Category("General")
    @ConfigEntry.Gui.Tooltip
    public double cam_dist = 4;
    @ConfigEntry.Category("General")
    @ConfigEntry.Gui.Tooltip
    public double cam_sens = 1;

    @ConfigEntry.Category("Cardinal")
    @ConfigEntry.Gui.Tooltip
    public boolean cardinal_global = false;
    @ConfigEntry.Category("Cardinal")
    @ConfigEntry.Gui.Tooltip
    public boolean cardinal_auto_look = true;

    public enum Cameras {
        VANILLA(OTPCam::new),
        LOCKED_FREE(LockedFreeCam::new),
        CARDINAL(CardinalCam::new);

        public final Supplier<OTPCam> cameraSupplier;
        Cameras(Supplier<OTPCam> cameraSupplier) {
            this.cameraSupplier = cameraSupplier;
        }
    }
}
