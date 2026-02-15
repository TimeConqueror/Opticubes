package io.socol.opticubes.fx;

public class RenderHelper {
    private static float translationX;
    private static float translationY;
    private static float translationZ;

    public static void setTranslation(float translationX, float translationY, float translationZ) {
        RenderHelper.translationX = translationX;
        RenderHelper.translationY = translationY;
        RenderHelper.translationZ = translationZ;
    }

    public static void addTranslation(float translationX, float translationY, float translationZ) {
        RenderHelper.translationX += translationX;
        RenderHelper.translationY += translationY;
        RenderHelper.translationZ += translationZ;
    }

    public static void resetTranslation() {
        RenderHelper.translationX = 0;
        RenderHelper.translationY = 0;
        RenderHelper.translationZ = 0;
    }
}
