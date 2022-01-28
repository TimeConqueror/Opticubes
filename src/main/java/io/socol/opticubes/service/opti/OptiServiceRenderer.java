package io.socol.opticubes.service.opti;

import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;

public class OptiServiceRenderer {

    public static void render(OptiService service, float partialTicks) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        ItemStack held = player.getHeldItem();
        if (!ItemOptiWrench.isOptiWrench(held)) {
            return;
        }

        BlockPos radiusEditingOptiCube = ClientOptiCubeEditingService.getInstance().getRadiusEditingOptiCube();

        if (!ClientOptiCubeEditingService.getInstance().isEditingRegion()) {
            for (OptiCube optiCube : service.getOptiCubes().values()) {
                if (!optiCube.isEnabled() && optiCube.getRegion().isInFrustum()) {
                    RegionRenderer.addRegion(optiCube.getRegion(), optiCube.getColor()).inflate(1 / 256f).ignoreDepth().withSides();
                }
                if (optiCube.hasExternalRegion() && (!optiCube.getPos().equals(radiusEditingOptiCube))) {
                    RegionRenderer.addRegion(new Region(optiCube.getPos()), optiCube.getColor()).inflate(1 / 266f).ignoreDepth();
                }
            }
        }
    }
}
