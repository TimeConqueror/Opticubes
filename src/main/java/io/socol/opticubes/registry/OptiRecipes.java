package io.socol.opticubes.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class OptiRecipes {
    public static void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(OptiBlocks.OPTICUBE),
                "#L#", "L L", "#L#",
                '#', "ingotIron", 'L', "gemLapis"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(OptiItems.OPTIWRENCH),
                " #L", " ##", "#  ",
                '#', "ingotIron", 'L', "gemLapis"
        ));
    }
}
