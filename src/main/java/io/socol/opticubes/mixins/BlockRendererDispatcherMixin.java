package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockRendererDispatcher.class)
public class BlockRendererDispatcherMixin {
    @Inject(method = "renderBlock", at = @At(value = "HEAD"), cancellable = true)
    public void opticubes$onRenderWorldBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> cir) {
        if (OptiCubes.getOptiClientService().skipSpecialBlockRender(state, pos)) {
            cir.setReturnValue(false);
        }
    }
}
