package io.socol.opticubes.blocks;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.registry.OptiItems;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockOptiCube extends Block implements ITileEntityProvider {

    public BlockOptiCube() {
        super(Material.rock);
        setHardness(2.5F);
        setResistance(10.0F);
        setStepSound(soundTypePiston);
        setBlockTextureName(OptiCubes.MODID + ":opticube");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityOptiCube();
    }

    @Override
    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player) {
        // works in survival gamemode only
        if (!player.isSneaking()) {
            return;
        }
        if (player.worldObj.isRemote) {
            return;
        }
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() == OptiItems.OPTIWRENCH) {
            OptiCubes.getEditingService().startSettingsEditingSession(
                (EntityPlayerMP) player,
                new BlockPos(x, y, z)
            );
        }
    }
}
