package io.socol.opticubes.tiles.render;

import io.socol.opticubes.tiles.TileEntityOptiCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityOptiCubeRenderer extends TileEntitySpecialRenderer<TileEntityOptiCube> {

    @Override
    public void render(TileEntityOptiCube optiCube, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;


//        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
//        if (player != null) {
//            ItemStack held = player.getHeldItem();
//            if (held != null && held.getItem() instanceof ItemOptiWrench) {
//                RegionRenderer.addRegion(optiCube.getAffectedRegion().move(tile.xCoord, tile.yCoord, tile.zCoord), 0xFFFFFFFF, 1/32f);
//            }
//        }
//        RegionRenderer.drawAll();
//        GL11.glTranslated(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);

//
//        OptiCubes.getService().drawRegions();
//
//        GL11.glTranslated(-x,- y, -z);

//        BoxRenderer.addBox(box.getOffsetBoundingBox(x, y, z), 0xFFFF0000);

//        for (int i = 0; i < 100; i++) {
//            int color = Color.getHSBColor((float) (Math.PI * 2f * i / 10f), 0.8f, 1f).getRGB();
//            BoxRenderer.addBox(box.getOffsetBoundingBox(x + i * 3, y, z), color | (0xFF << 24));
//        }

//        RegionRenderer.drawAll();
    }
}
