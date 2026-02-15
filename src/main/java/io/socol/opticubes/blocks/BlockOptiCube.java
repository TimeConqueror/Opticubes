package io.socol.opticubes.blocks;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.registry.OptiItems;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOptiCube extends Block implements ITileEntityProvider {

    public BlockOptiCube() {
        super(Material.ROCK);
        setHardness(2.5F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityOptiCube();
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        // works in survival gamemode only
        if (!player.isSneaking()) {
            return;
        }
        if (world.isRemote) {
            return;
        }
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem != null && heldItem.getItem() == OptiItems.OPTIWRENCH) {
            OptiCubes.getEditingService().startSettingsEditingSession(
                    (EntityPlayerMP) player,
                    pos
            );
        }
    }

}
