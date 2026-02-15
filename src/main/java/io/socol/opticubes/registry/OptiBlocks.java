package io.socol.opticubes.registry;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.blocks.BlockOptiCube;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.tiles.render.TileEntityOptiCubeRenderer;
import io.socol.opticubes.utils.EasyRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OptiBlocks {

    private static final EasyRegistry REGISTRY = new EasyRegistry(OptiCubes.MODID, CreativeTabs.TOOLS);

    public static final Block OPTICUBE = new BlockOptiCube();

    public static void register() {
        REGISTRY.registerBlockWithItem(OPTICUBE, "opticube", new ItemBlock(OPTICUBE));
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers() {
        REGISTRY.registerBlockRender(OPTICUBE);
    }
}
