package io.socol.opticubes.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.blocks.BlockOptiCube;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class OptiBlocks {

    public static final Block OPTICUBE = new BlockOptiCube();

    public static void register() {
        registerBlock(OPTICUBE, "opticube");
    }

    private static void registerBlock(Block block, String id) {
        block.setUnlocalizedName(OptiCubes.MODID + "." + id);
        block.setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(block, id);
    }
}
