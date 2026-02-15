package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ParticleManager.class, priority = 1000000000)
public class ParticleManagerMixin {

    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    public void opticubes$onAddEffect(Particle particle, CallbackInfo ci) {
        if (OptiCubes.getOptiClientService().skipParticleSpawn(particle)) {
            ci.cancel();
        }
    }
}
