package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderGlobal.class, priority = 1000000000)
public class RenderGlobalMixin {

    @Inject(method = "spawnParticle0(IZZDDDDDD[I)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    public void opticubes$cancelSpawnParticles(int particleID, boolean ignoreRange, boolean minParticles, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters, CallbackInfoReturnable<Particle> cir) {
        if (OptiCubes.getOptiClientService().skipParticleSpawn(new BlockPos(xCoord, yCoord, zCoord))) {
            cir.setReturnValue(null);
        }
    }
}
