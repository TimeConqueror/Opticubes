package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin {

    @Shadow
    public static TileEntityRendererDispatcher instance;

    @Inject(method = "renderTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getLightBrightnessForSkyBlocks(IIII)I", shift = At.Shift.BEFORE), cancellable = true)
    public void onTileRender(TileEntity tile, float partialTicks, CallbackInfo ci) {
        if (OptiCubes.getService().skipTileRender(tile)) {
            ci.cancel();
        }
    }
}
