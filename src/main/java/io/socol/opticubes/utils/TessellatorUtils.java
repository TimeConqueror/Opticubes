package io.socol.opticubes.utils;

import net.minecraft.client.renderer.Tessellator;

public class TessellatorUtils {

    public static void setColor(int color) {
        Tessellator.instance.setColorRGBA(
                (color >> 16) & 0xFF,
                (color >> 8) & 0xFF,
                color & 0xFF,
                (color >> 24) & 0xFF
        );
    }
}
