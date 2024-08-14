package github.pitbox46.openthirdperson.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import github.pitbox46.openthirdperson.client.camera.OTPCam;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

public class OpenThirdPersonClient implements ClientModInitializer {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping CAM_BUTTON = new KeyMapping("key.openthirdperson.cam_button", InputConstants.Type.MOUSE, 2, "key.openthirdperson.category");
    public static OTPCam otpCam = new OTPCam();
    public static Config config;
    public static Config.Cameras lastCamera = Config.Cameras.VANILLA;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(CAM_BUTTON);
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(Config.class).getConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (lastCamera != config.camera) {
                lastCamera = config.camera;
                otpCam = lastCamera.cameraSupplier.get();
            }
        });
    }
}
