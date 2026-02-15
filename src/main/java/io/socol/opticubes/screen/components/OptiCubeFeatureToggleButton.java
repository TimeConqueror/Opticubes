package io.socol.opticubes.screen.components;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.OptiFeature;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeSettingsEditingSession;
import io.socol.opticubes.service.opti.OptiCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class OptiCubeFeatureToggleButton extends GuiButton {

    private static final ResourceLocation TEXTURE = new ResourceLocation(OptiCubes.MODID, "textures/gui/opti_cube_settings.png");

    private final OptiCubeSettingsEditingSession session;
    private final OptiFeature feature;

    public OptiCubeFeatureToggleButton(int buttonId, int x, int y, OptiCubeSettingsEditingSession session, OptiFeature feature) {
        super(buttonId, x, y, "");
        this.session = session;
        this.feature = feature;
        this.width = 20;
        this.height = 22;
    }

    public OptiFeature getFeature() {
        return feature;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        //FIXME check
//        if (!this.visible) {
//            return;
//        }
//        mc.getTextureManager().bindTexture(TEXTURE);
//        this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
//        boolean hovered = field_146123_n;
//        boolean featureEnabled = session.isFeatureEnabled(feature);
//        int tx = hovered ? 20 : 0;
//        int ty = 46 + (featureEnabled ? 22 : 0);
//        this.drawTexturedModalRect(this.xPosition, this.yPosition, tx, ty, this.width, this.height);
//        this.drawTexturedModalRect(
//                this.xPosition + 2,
//                this.yPosition + 2 + (featureEnabled ? 1 : 0),
//                feature.ordinal()  * 16, 90,
//                16, 16
//        );
    }

    public void onClick() {
        session.toggleFeature(feature);
    }
}
