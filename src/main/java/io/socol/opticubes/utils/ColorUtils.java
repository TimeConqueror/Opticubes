package io.socol.opticubes.utils;

import net.minecraft.client.renderer.GlStateManager;

public class ColorUtils {

    public static int interpolateColor(int a, int b, float percent) {
        if (percent <= 0) {
            return a;
        } else if (percent >= 1) {
            return b;
        }

        int ra = (a >> 16) & 0xFF;
        int ga = (a >> 8) & 0xFF;
        int ba = a & 0xFF;
        int aa = a >> 24;

        int rb = (b >> 16) & 0xFF;
        int gb = (b >> 8) & 0xFF;
        int bb = b & 0xFF;
        int ab = b >> 24;

        float p1 = 1 - percent;

        int rc = (int) (p1 * ra + percent * rb);
        int gc = (int) (p1 * ga + percent * gb);
        int bc = (int) (p1 * ba + percent * bb);
        int ac = (int) (p1 * aa + percent * ab);

        return (ac << 24) + (rc << 16) + (gc << 8) + bc;
    }

    public static int withAlpha(int color, double alpha) {
        return (color & 0xFFFFFF) | ((int) (255 * alpha) << 24);
    }

    /**
     * Returns red channel data of the ARGB color.
     */
    public static int getRed(int argb) {
        return argb >> 16 & 0xFF;
    }

    /**
     * Returns green channel data of the ARGB color.
     */
    public static int getGreen(int argb) {
        return argb >> 8 & 0xFF;
    }

    /**
     * Returns blue channel data of the ARGB color.
     */
    public static int getBlue(int argb) {
        return argb & 0xFF;
    }

    /**
     * Returns alpha channel data of the ARGB color.
     */
    public static int getAlpha(int argb) {
        return argb >> 24 & 0xFF;
    }

    public static void setGlColor(int argb) {
        GlStateManager.color(ColorUtils.getRed(argb) / 255F, ColorUtils.getGreen(argb) / 255F, ColorUtils.getBlue(argb) / 255F, ColorUtils.getAlpha(argb) / 255F);
    }
}
