package io.socol.opticubes.mixins;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(value = RenderGlobal.class, priority = 1000000000)
public class RenderGlobalMixin {

    @Inject(method = "doSpawnParticle", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void opticubes$cancelSpawnParticles(String particleType, double x, double y, double z, double velX, double velY, double velZ, CallbackInfoReturnable<EntityFX> cir) {
        if(OptiCubes.getOptiClientService().skipParticleSpawn(new BlockPos(x, y, z))) {
            cir.setReturnValue(null);
        }
    }
}
