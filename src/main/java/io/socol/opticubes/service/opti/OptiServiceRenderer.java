package io.socol.opticubes.service.opti;

import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import io.socol.opticubes.utils.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

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

        for (OptiCube optiCube : service.getOptiCubes().values()) {
            if (optiCube.getRegion().isInFrustum()) {
                boolean editing = ClientOptiCubeEditingService.getInstance().isEditingRegion(
                        player.getEntityWorld(), optiCube.getPos(), OptiCubeRegionType.AFFECTED_REGION
                );

                if (editing) {
                    if (!optiCube.getRegion().equals(optiCube.getPos())) {
                        RegionRenderer.addRegion(new Region(optiCube.getPos()), 0xFF1CDD7A, 1 / 64f);
                    }
                    float time = player.ticksExisted + partialTicks;
                    float animation = MathHelper.sin((float) Math.toRadians(time * 20));
                    RegionRenderer.addRegion(optiCube.getRegion(), 0xFF1CDD7A, 1 / 16f + animation * 1 / 32f);
                } else {
                    int color = optiCube.isEnabled() ? 0xFF808080 : 0xFF3590FF;
                    RegionRenderer.addRegion(optiCube.getRegion(), color, 1 / 32f);
                }
            }
        }
    }
}
