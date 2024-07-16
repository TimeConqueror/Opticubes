package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(value = RenderManager.class, priority = 1000000000)
public class RenderManagerMixin {

    @Inject(method = "renderEntityStatic", at = @At("HEAD"), cancellable = true)
    public void opticubes$onRenderEntityStatic(Entity entity, float partialTicks, boolean p_147936_3_, CallbackInfoReturnable<Boolean> ci) {
        if (OptiCubes.getOptiClientService().skipEntityRender(entity)) {
            ci.setReturnValue(true);
        }
    }
}
