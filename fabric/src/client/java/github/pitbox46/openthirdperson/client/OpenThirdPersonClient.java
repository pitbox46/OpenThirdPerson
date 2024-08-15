package github.pitbox46.openthirdperson.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import github.pitbox46.openthirdperson.client.camera.OTPCam;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.slf4j.Logger;

public class OpenThirdPersonClient implements ClientModInitializer {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping CAM_BUTTON = new KeyMapping("key.openthirdperson.cam_button", InputConstants.Type.MOUSE, 2, "key.openthirdperson.category");
    public static OTPCam otpCam = new OTPCam();

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(CAM_BUTTON);
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, Config.SPEC);
        ConfigScreenFactoryRegistry.INSTANCE.register(MODID, ConfigurationScreen::new);

        NeoForgeModConfigEvents.loading(MODID).register(config -> {
            otpCam = Config.CAMERA.get().cameraSupplier.get();
        });
        NeoForgeModConfigEvents.reloading(MODID).register(config -> {
            otpCam = Config.CAMERA.get().cameraSupplier.get();
        });
    }
}
