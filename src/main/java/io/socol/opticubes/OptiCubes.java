package io.socol.opticubes;

import io.socol.opticubes.proxy.CommonProxy;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeEditingService;
import io.socol.opticubes.service.opti.OptiClientService;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.socol.opticubes.utils.Config;

@Mod(modid = OptiCubes.MODID, version = OptiCubes.VERSION, dependencies = "required:mixinbooter@[10.6,);")
public class OptiCubes {
    public static final String MODID = "opticubes";
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final Logger LOGGER = LogManager.getLogger("OptiCubes");

    @SidedProxy(clientSide = "io.socol.opticubes.proxy.ClientProxy", serverSide = "io.socol.opticubes.proxy.CommonProxy")
    public static CommonProxy proxy;

    private static OptiClientService optiClientService = null;
    private static OptiCubeEditingService editingService;

    public OptiCubes() {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            optiClientService = new OptiClientService();
            editingService = new ClientOptiCubeEditingService();
        } else {
            editingService = new OptiCubeEditingService();
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        Config.setConfigDir(event.getModConfigurationDirectory());
        OCConfigs.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static OptiClientService getOptiClientService() {
        return optiClientService;
    }

    public static OptiCubeEditingService getEditingService() {
        return editingService;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MODID, path);
    }
}
