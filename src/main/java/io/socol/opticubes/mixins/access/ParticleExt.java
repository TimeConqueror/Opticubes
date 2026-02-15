package io.socol.opticubes.mixins.access;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleExt {
    @Accessor
    double getPosX();

    @Accessor
    double getPosY();

    @Accessor
    double getPosZ();
}

