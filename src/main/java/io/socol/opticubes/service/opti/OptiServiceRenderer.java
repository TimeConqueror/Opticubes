package io.socol.opticubes.service.opti;

import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.mixins.access.RenderManagerExt;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.utils.ClientMixinAccessor;
import io.socol.opticubes.utils.ColorUtils;
import io.socol.opticubes.utils.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class OptiServiceRenderer {

    public static void render(OptiClientService service, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager renderManager = mc.getRenderManager();
        EntityPlayerSP player = mc.player;

        if (player == null) {
            return;
        }

        ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!ItemOptiWrench.isOptiWrench(held)) {
            return;
        }

        BlockPos radiusEditingOptiCube = ClientOptiCubeEditingService.getInstance().getRadiusEditingOptiCube();

        if (!ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
            for (OptiCube optiCube : service.getOptiCubes().values()) {
                if(!isInFrustum(renderManager, optiCube.getRegion())) {
                    continue;
                }

                if (!optiCube.isEnabled()) {
                    double alpha = optiCube.getRadius() <= 0 ? 1.0 : (1.0 - 0.88 * optiCube.getDistance() / optiCube.getRadius());
                    RegionRenderer.addRegion(optiCube.getRegion(), ColorUtils.withAlpha(optiCube.getColor(), alpha)).inflate(1 / 256f).ignoreDepth().withSides();
                } else if (optiCube.getRadius() < 3 && optiCube.getDistance() <= 3) {
                    RegionRenderer.FXRegion fxRegion = RegionRenderer.addRegion(optiCube.getRegion(), 0xFF808080).inflate(1 / 256f).ignoreDepth();
                    if (optiCube.getRadius() == -1) {
                        fxRegion.withSides();
                    }
                }

                if (optiCube.hasExternalRegion() && (!optiCube.getPos().equals(radiusEditingOptiCube))) {
                    RegionRenderer.addRegion(new Region(optiCube.getPos()), optiCube.getColor()).inflate(1 / 266f).ignoreDepth();
                }
            }
        }
    }

    private static boolean isInFrustum(RenderManager manager, Region region) {
        RenderManagerExt ext = ClientMixinAccessor.get(manager);

        return ClippingHelperImpl.getInstance().isBoxInFrustum(
                region.x0 - ext.getRenderPosX(),
                region.y0 - ext.getRenderPosY(),
                region.z0 - ext.getRenderPosZ(),
                region.x1 - ext.getRenderPosX(),
                region.y1 - ext.getRenderPosY(),
                region.z1 - ext.getRenderPosZ()
        );
    }
}
