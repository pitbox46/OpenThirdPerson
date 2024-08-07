package github.pitbox46.openthirdperson;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.util.Lazy;
import org.slf4j.Logger;

@Mod(OpenThirdPerson.MODID)
public class OpenThirdPerson {
    public static final String MODID = "openthirdperson";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Lazy<KeyMapping> MOVE_CAM = Lazy.of(() -> new KeyMapping("key.openthirdperson.movecam", InputConstants.Type.MOUSE, 2, "key.openthirdperson.category"));
    public static final CameraData CAMERA_DATA = new CameraData();

    public OpenThirdPerson(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void setCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            if (CameraData.shouldUseLockCam()) {
                event.setPitch((float) CAMERA_DATA.pitch);
                event.setYaw((float) CAMERA_DATA.yaw);
                event.setRoll((float) CAMERA_DATA.roll);
            }
        }

        @SubscribeEvent
        public static void setDetachedCameraDist(CalculateDetachedCameraDistanceEvent event) {
            event.setDistance(Config.CAM_DIST.get().floatValue());
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = MODID)
    static class ClientModEvents {
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(MOVE_CAM.get());
        }
    }
}
