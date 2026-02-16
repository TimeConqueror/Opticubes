package io.socol.opticubes.mixins;

import io.socol.opticubes.OCConfigs;
import io.socol.opticubes.OptiCubes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = BlockRendererDispatcher.class)
public class BlockRendererDispatcherMixin {

    @Inject(method = "renderBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Z)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void opticubes$onRenderWorldBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> cir, EnumBlockRenderType enumblockrendertype, IBakedModel model) {
        if(!OCConfigs.specialBlocksFeatureEnabled()) {
            return;
        }

        if (OptiCubes.getOptiClientService().skipSpecialBlockRender(state, pos, model)) {
            cir.setReturnValue(false);
        }
    }
}
