package io.socol.opticubes.mixins;

import cpw.mods.fml.client.registry.RenderingRegistry;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderingRegistry.class, remap = false)
public class RenderingRegistryMixin {

    @Inject(method = "renderWorldBlock", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/client/registry/ISimpleBlockRenderingHandler;renderWorldBlock(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraft/block/Block;ILnet/minecraft/client/renderer/RenderBlocks;)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void onRenderWorldBlock(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block, int modelId, CallbackInfoReturnable<Boolean> cir) {
        if (OptiCubes.getOptiService().skipBlockRender(block, new BlockPos(x, y, z))) {
            cir.setReturnValue(false);
        }
    }
}
