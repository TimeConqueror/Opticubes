package io.socol.opticubes.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.registry.OptiTiles;
import io.socol.opticubes.utils.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

public class ClientProxy extends CommonProxy {
    @Nullable
    public static World world() {
        return Hacks.safeCast(Minecraft.getMinecraft().theWorld);
    }

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

        MinecraftForge.EVENT_BUS.register(new RegionRenderer.EventListener());

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
