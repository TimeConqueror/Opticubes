package io.socol.opticubes.registry;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.tiles.render.TileEntityOptiCubeRenderer;

public class OptiTiles {

    public static void register() {
        GameRegistry.registerTileEntity(TileEntityOptiCube.class, "opticube");
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOptiCube.class, new TileEntityOptiCubeRenderer());
    }
}
