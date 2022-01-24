package io.socol.opticubes.blocks;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockOptiCube extends Block implements ITileEntityProvider {

    public BlockOptiCube() {
        super(Material.rock);
        setHardness(2.5F);
        setResistance(10.0F);
        setStepSound(soundTypePiston);
        setTextureName(OptiCubes.MODID + ":opticube");
//        setTextureName("")
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityOptiCube();
    }
}
