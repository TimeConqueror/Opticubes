package io.socol.opticubes.items;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.registry.OptiBlocks;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemOptiWrench extends Item {

    public ItemOptiWrench() {
        setTextureName(OptiCubes.MODID + ":optiwrench");
        setFull3D();
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (world.isRemote && ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
                ClientOptiCubeEditingService.getInstance().stopRegionEditingSession(null);
                return true;
            }
            if (!world.isRemote && world.getBlock(x, y, z) == OptiBlocks.OPTICUBE) {
                OptiCubes.getEditingService().startRegionEditingSession((EntityPlayerMP) player, new BlockPos(x, y, z), OptiCubeRegionType.AFFECTED_REGION);
                return true;
            }
        } else {
            if (world.isRemote && ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
                ClientOptiCubeEditingService.getInstance().addRegionPoint(new BlockPos(x, y, z));
                return true;
            }
        }
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List target, boolean advancedTooltips) {
        List<String> tooltip = (List<String>) target;
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.held"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.start_region_editing"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.stop_region_editing"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.create_region"));
        tooltip.add(I18n.format("item.opticubes.optiwrench.usage.change_radius"));
    }

    public static boolean isOptiWrench(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemOptiWrench;
    }
}
