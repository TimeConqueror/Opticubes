package io.socol.opticubes.registry;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.tiles.render.TileEntityOptiCubeRenderer;
import io.socol.opticubes.utils.EasyRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OptiTiles {

    private static final EasyRegistry REGISTRY = new EasyRegistry(OptiCubes.MODID, CreativeTabs.TOOLS);

    public static void register() {
        REGISTRY.registerTileEntity(TileEntityOptiCube.class, "opticube");
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers() {
        REGISTRY.registerTESR(TileEntityOptiCube.class, new TileEntityOptiCubeRenderer());
    }
}
