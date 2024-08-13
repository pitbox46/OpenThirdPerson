package github.pitbox46.openthirdperson;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import github.pitbox46.openthirdperson.camera.OTPCam;
import net.minecraft.client.KeyMapping;
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
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Vector3f;
import org.slf4j.Logger;

@Mod(OpenThirdPerson.MODID)
public class OpenThirdPerson {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Lazy<KeyMapping> MOVE_CAM = Lazy.of(() -> new KeyMapping("key.openthirdperson.movecam", InputConstants.Type.MOUSE, 2, "key.openthirdperson.category"));
    public static OTPCam otpCam = new OTPCam();

    public OpenThirdPerson(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onInputUpdate(MovementInputUpdateEvent event) {
            if (OTPCam.isCamDetached()) {
                otpCam.handlePlayerMovement(event.getInput());
            }
        }

        @SubscribeEvent
        public static void onInteractionStart(InputEvent.InteractionKeyMappingTriggered event) {
            if (OTPCam.isCamDetached() && event.isAttack() || event.isUseItem()) {
                otpCam.handleInteraction();
            }
        }

        @SubscribeEvent
        public static void setCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            if (OTPCam.isCamDetached()) {
                Vector3f angles = otpCam.computeAngles(
                        event.getCamera(),
                        new Vector3f(event.getYaw(), event.getPitch(), event.getRoll())
                );
                event.setYaw(angles.x);
                event.setPitch(angles.y);
                event.setRoll(angles.z);
            }
        }

        @SubscribeEvent
        public static void setDetachedCameraDist(CalculateDetachedCameraDistanceEvent event) {
            event.setDistance(otpCam.computeDist(event.getCamera(), event.getDistance()));
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = MODID)
    static class ClientModEvents {
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(MOVE_CAM.get());
        }

        @SubscribeEvent
        public static void configListener(ModConfigEvent.Loading event) {
            otpCam = Config.CAMERA.get().cameraSupplier.get();
        }

        @SubscribeEvent
        public static void configListener(ModConfigEvent.Reloading event) {
            otpCam = Config.CAMERA.get().cameraSupplier.get();
        }
    }
}
