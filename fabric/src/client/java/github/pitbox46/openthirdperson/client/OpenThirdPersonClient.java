package github.pitbox46.openthirdperson.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import github.pitbox46.openthirdperson.client.camera.OTPCam;
import github.pitbox46.openthirdperson.client.camera.TransitionCam;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

public class OpenThirdPersonClient implements ClientModInitializer {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping CAM_BUTTON = new KeyMapping("key.openthirdperson.cam_button", InputConstants.Type.KEYSYM, 342, "key.openthirdperson.category");
    /**
     * The camera normally in use
     */
    public static OTPCam normalOTPCam = null;
    /**
     * Camera when player is a passenger
     */
    public static OTPCam rideOTPCam = null;
    /**
     * Current selected camera
     */
    public static TransitionCam camera = new TransitionCam(new OTPCam());

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(CAM_BUTTON);
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, Config.SPEC);

        ClientTickEvents.START_CLIENT_TICK.register((client -> {
            if (normalOTPCam == null || rideOTPCam == null) {
                normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
                rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
            }
            if (client.player != null) {
                if (client.player.isPassenger()) {
                    camera.changeCamera(rideOTPCam);
                } else {
                    camera.changeCamera(normalOTPCam);
                }
                camera.tick();
            }
        }));

        ModConfigEvents.loading(MODID).register(config -> {
            normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
            rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
            camera = new TransitionCam(normalOTPCam);
        });
        ModConfigEvents.reloading(MODID).register(config -> {
            normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
            rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
            camera = new TransitionCam(normalOTPCam);
        });
    }
}
