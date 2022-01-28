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
                int color = optiCube.isEnabled() ? 0xFF808080 : 0xFF3590FF;
                if (optiCube.getRegion().isInFrustum()) {
//                    boolean editing = ClientOptiCubeEditingService.getInstance().isEditingRegion(
//                            player.getEntityWorld(), optiCube.getPos(), OptiCubeRegionType.AFFECTED_REGION
//                    );
//
//                    if (editing) {
//                        if (!optiCube.getRegion().equals(optiCube.getPos())) {
//                            RegionRenderer.addRegion(new Region(optiCube.getPos()), 0xFF1CDD7A).inflate(1 / 64f);
//                        }
//                        float time = player.ticksExisted + partialTicks;
//                        float animation = MathHelper.sin((float) Math.toRadians(time * 20));
//                        RegionRenderer.addRegion(optiCube.getRegion(), 0xFF1CDD7A).inflate(1 / 16f + animation * 1 / 32f).ignoreDepth();
//                    } else {

                    RegionRenderer.addRegion(optiCube.getRegion(), color).inflate(1 / 256f).ignoreDepth();
//                    }
                }
                if (optiCube.hasExternalRegion() && (!optiCube.getPos().equals(radiusEditingOptiCube))) {
                    RegionRenderer.addRegion(new Region(optiCube.getPos()), color).inflate(1 / 266f).ignoreDepth();
                }
            }
        }
    }
}
