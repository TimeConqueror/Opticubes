package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(EffectRenderer.class)
public class EffectRendererMixin {

    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    public void opticubes$onAddEffect(EntityFX particle, CallbackInfo ci) {
        if (OptiCubes.getOptiClientService().skipSpawnParticle(particle)) {
            ci.cancel();
        }
    }
}
