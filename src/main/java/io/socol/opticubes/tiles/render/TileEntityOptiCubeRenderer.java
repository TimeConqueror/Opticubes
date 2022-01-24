package io.socol.opticubes.tiles.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class TileEntityOptiCubeRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileEntityOptiCube optiCube = (TileEntityOptiCube) tile;
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

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
