package io.socol.opticubes.items;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.registry.OptiBlocks;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemOptiWrench extends Item {

    public ItemOptiWrench() {
        setFull3D();
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) {
            if (world.isRemote && ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
                ClientOptiCubeEditingService.getInstance().stopRegionEditingSession(null);
                return EnumActionResult.SUCCESS;
            }
            if (!world.isRemote && world.getBlockState(pos).getBlock() == OptiBlocks.OPTICUBE) {
                OptiCubes.getEditingService().startRegionEditingSession((EntityPlayerMP) player, pos, OptiCubeRegionType.AFFECTED_REGION);
                return EnumActionResult.SUCCESS;
            }
        } else {
            if (world.isRemote && ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
                ClientOptiCubeEditingService.getInstance().addRegionPoint(pos);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World level, EntityPlayer player, EnumHand hand) {
        if (level.isRemote && ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
            RayTraceResult hitResult = Minecraft.getMinecraft().objectMouseOver;
            if (hitResult != null && hitResult.typeOfHit == RayTraceResult.Type.MISS) {
                if (player.isSneaking()) {
                    ClientOptiCubeEditingService.getInstance().stopRegionEditingSession(null);
                } else {
                    ClientOptiCubeEditingService.getInstance().addRegionPoint(hitResult.getBlockPos());
                }
            }
        }
        return super.onItemRightClick(level, player, hand);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        // works in creative gamemode only
        if (!player.isSneaking()) {
            return super.onBlockStartBreak(itemstack, pos, player);
        }
        World world = player.world;
        if (world.getBlockState(pos).getBlock() != OptiBlocks.OPTICUBE) {
            return super.onBlockStartBreak(itemstack, pos, player);
        }
        if (!player.world.isRemote) {
            OptiCubes.getEditingService().startSettingsEditingSession(
                    (EntityPlayerMP) player,
                    pos
            );
        }
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.held"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.start_region_editing"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.stop_region_editing"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.create_region"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.change_radius"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.start_settings_editing"));
    }

    public static boolean isOptiWrench(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemOptiWrench;
    }
}
