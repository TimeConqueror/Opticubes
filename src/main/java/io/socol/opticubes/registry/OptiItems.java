package io.socol.opticubes.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.items.ItemOptiWrench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class OptiItems {

    public static final Item OPTIWRENCH = new ItemOptiWrench();

    public static void register() {
        registerItem(OPTIWRENCH, "optiwrench");
    }

    private static void registerItem(Item item, String id) {
        item.setUnlocalizedName(OptiCubes.MODID + "." + id);
        item.setCreativeTab(CreativeTabs.tabTools);
        GameRegistry.registerItem(item, id);
    }
}
