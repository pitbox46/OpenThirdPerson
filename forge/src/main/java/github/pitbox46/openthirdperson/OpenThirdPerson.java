package github.pitbox46.openthirdperson;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import github.pitbox46.openthirdperson.camera.OTPCam;
import github.pitbox46.openthirdperson.camera.TransitionCam;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.joml.Vector3f;
import org.slf4j.Logger;

@Mod(OpenThirdPerson.MODID)
public class OpenThirdPerson {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Lazy<KeyMapping> CAM_BUTTON = Lazy.of(() -> new KeyMapping("key.openthirdperson.cam_button", InputConstants.Type.KEYSYM, 342, "key.openthirdperson.category"));
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
    public static TransitionCam camera = new TransitionCam(normalOTPCam);

    public OpenThirdPerson() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                return;
            }
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                if (player.isPassenger()) {
                    camera.changeCamera(rideOTPCam);
                } else {
                    camera.changeCamera(normalOTPCam);
                }
                camera.tick();
            }
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
    static class ClientModEvents {
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(CAM_BUTTON.get());
        }

        @SubscribeEvent
        public static void configListener(ModConfigEvent.Loading event) {
            normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
            rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
            camera = new TransitionCam(normalOTPCam);
        }

        @SubscribeEvent
        public static void configListener(ModConfigEvent.Reloading event) {
            normalOTPCam = Config.CAMERA.get().cameraSupplier.get();
            rideOTPCam = Config.RIDE_CAMERA.get().cameraSupplier.get();
            camera = new TransitionCam(normalOTPCam);
        }
    }
}
