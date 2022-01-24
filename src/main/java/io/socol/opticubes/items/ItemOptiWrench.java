package io.socol.opticubes.items;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.service.OptiWrench;
import net.minecraft.item.Item;

public class ItemOptiWrench extends Item implements OptiWrench {

    public ItemOptiWrench() {
        setTextureName(OptiCubes.MODID + ":optiwrench");
        setFull3D();
    }
}
