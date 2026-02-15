package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin {
    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "HEAD"), cancellable = true)
    public void opticubes$onTileRender(TileEntity tileentityIn, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (OptiCubes.getOptiClientService().skipTileRender(tileentityIn)) {
            ci.cancel();
        }
    }
}
