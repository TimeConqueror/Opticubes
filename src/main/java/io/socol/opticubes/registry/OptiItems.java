package io.socol.opticubes.registry;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.utils.EasyRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class OptiItems {
    private static final EasyRegistry REGISTRY = new EasyRegistry(OptiCubes.MODID, CreativeTabs.TOOLS);

    public static final Item OPTIWRENCH = new ItemOptiWrench();

    public static void register() {
        REGISTRY.registerItem(OPTIWRENCH, "optiwrench");
    }
}
