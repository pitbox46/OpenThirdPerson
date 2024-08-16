package github.pitbox46.openthirdperson.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import github.pitbox46.openthirdperson.client.camera.OTPCam;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.slf4j.Logger;

public class OpenThirdPersonClient implements ClientModInitializer {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping CAM_BUTTON = new KeyMapping("key.openthirdperson.cam_button", InputConstants.Type.MOUSE, 2, "key.openthirdperson.category");
    /**
     * The camera normally in use
     */
    public static OTPCam normalOTPCam = new OTPCam();
    /**
     * Camera when player is a passenger
     */
    public static OTPCam rideOTPCam = new OTPCam();
    /**
     * Current selected camera
     */
    public static OTPCam currentOTPCam = normalOTPCam;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(CAM_BUTTON);
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, Config.SPEC);
        ConfigScreenFactoryRegistry.INSTANCE.register(MODID, ConfigurationScreen::new);

        ClientTickEvents.START_CLIENT_TICK.register((client -> {
            if (client.player != null && client.player.isPassenger()) {
                currentOTPCam = rideOTPCam;
            } else {
                currentOTPCam = normalOTPCam;
            }
        }));

        NeoForgeModConfigEvents.loading(MODID).register(config -> {
            normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
            rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
        });
        NeoForgeModConfigEvents.reloading(MODID).register(config -> {
            normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
            rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
        });
    }
}
