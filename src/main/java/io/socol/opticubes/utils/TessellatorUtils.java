package io.socol.opticubes.utils;

import net.minecraft.client.renderer.GlStateManager;

public class TessellatorUtils {

    public static void setColor(int color) {
        GlStateManager.color(
                ((color >> 16) & 0xFF) / 255F,
                ((color >> 8) & 0xFF) / 255F,
                (color & 0xFF) / 255F,
                ((color >> 24) & 0xFF) / 255F
        );
    }
}
