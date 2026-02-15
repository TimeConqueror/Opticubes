package io.socol.opticubes.screen;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.OptiFeature;
import io.socol.opticubes.screen.components.OptiCubeFeatureToggleButton;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeSettingsEditingSession;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class OptiCubeSettingsScreen extends GuiScreen {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(OptiCubes.MODID, "textures/gui/opti_cube_settings.png");
    private static final int BACKGROUND_WIDTH = 114;
    private static final int BACKGROUND_HEIGHT = 46;

    private final OptiCubeSettingsEditingSession session;

    public OptiCubeSettingsScreen(OptiCubeSettingsEditingSession session) {
        this.session = session;
    }

    @Override
    public void initGui() {
        int x0 = (this.width - BACKGROUND_WIDTH) / 2;
        int y0 = (this.height - BACKGROUND_HEIGHT) / 2;
        for (OptiFeature feature : OptiFeature.values()) {
            int index = feature.ordinal();
            this.buttonList.add(new OptiCubeFeatureToggleButton(
                index,
                x0 + 3 + index * 22,
                y0 + 17,
                session,
                feature
            ));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button instanceof OptiCubeFeatureToggleButton) {
            ((OptiCubeFeatureToggleButton) button).onClick();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int x0 = (this.width - BACKGROUND_WIDTH) / 2;
        int y0 = (this.height - BACKGROUND_HEIGHT) / 2;
        this.drawTexturedModalRect(x0, y0, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        OptiFeature hoveredFeature = getHoveredFeature();
        if (hoveredFeature != null) {
            mc.fontRenderer.drawString(I18n.format(hoveredFeature.getLangKey()), x0 + 5, y0 + 5, 0xFF7A7A7A/*0xFF6C8FE2*/);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private OptiFeature getHoveredFeature() {
        for (GuiButton guiButton : buttonList) {
            if (guiButton instanceof OptiCubeFeatureToggleButton) {
                OptiCubeFeatureToggleButton toggleButton = (OptiCubeFeatureToggleButton) guiButton;
                if (toggleButton.isMouseOver()) {
                    return toggleButton.getFeature();
                }
            }
        }
        return null;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ClientOptiCubeEditingService.getInstance().stopSettingsEditingSession(
            session.getOptiCubePos(),
            session.getFeaturesMask()
        );
    }
}
