package github.pitbox46.openthirdperson;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import github.pitbox46.openthirdperson.camera.OTPCam;
import github.pitbox46.openthirdperson.camera.TransitionCam;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.util.Lazy;
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

    public OpenThirdPerson(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Pre event) {
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

        @SubscribeEvent
        public static void onInputUpdate(MovementInputUpdateEvent event) {
            if (camera.isCamDetached()) {
                camera.handlePlayerMovement(event.getInput());
            }
        }

        @SubscribeEvent
        public static void onInteractionStart(InputEvent.InteractionKeyMappingTriggered event) {
            if (camera.isCamDetached() && event.isAttack() || event.isUseItem()) {
                camera.handleInteraction();
            }
        }

        @SubscribeEvent
        public static void setCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            if (camera.isCamDetached()) {
                Vector3f angles = camera.computeAngles(
                        event.getCamera(),
                        new Vector3f(event.getYaw(), event.getPitch(), event.getRoll()),
                        (float) event.getPartialTick()
                );
                event.setYaw(angles.x);
                event.setPitch(angles.y);
                event.setRoll(angles.z);
            }
        }

        @SubscribeEvent
        public static void setDetachedCameraDist(CalculateDetachedCameraDistanceEvent event) {
            event.setDistance(camera.computeDist(event.getCamera(), event.getDistance()));
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = MODID)
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
