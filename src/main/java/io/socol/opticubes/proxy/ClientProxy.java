package io.socol.opticubes.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.socol.opticubes.registry.OptiTiles;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        OptiTiles.registerRenderers();

//        MinecraftForge.EVENT_BUS.register(this);
//
//        ModItems.registerRenders();
//        ModBlocks.registerRenders();
//        ModTileEntities.registerRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

//        ModKeyBindings.init();
//
//        ModItems.registerItemColors();
//        ModBlocks.registerBlockColors();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

//        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
//
//        if (ModConfig.renderWearingBackpacks) {
//            RenderPlayer def = rm.getSkinMap().get("default");
//            def.addLayer(new LayerBackpack(def));
//
//            RenderPlayer slim = rm.getSkinMap().get("slim");
//            slim.addLayer(new LayerBackpack(slim));
//        }
    }
}
