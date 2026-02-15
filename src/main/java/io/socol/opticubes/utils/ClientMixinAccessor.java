package io.socol.opticubes.utils;

import io.socol.opticubes.mixins.access.ParticleExt;
import io.socol.opticubes.mixins.access.RenderGlobalExt;
import io.socol.opticubes.mixins.access.RenderManagerExt;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;

public class ClientMixinAccessor {
    public static RenderManagerExt get(RenderManager manager) {
        return ((RenderManagerExt) ((Object) manager));
    }

    public static ParticleExt get(Particle particle) {
        return ((ParticleExt) ((Object) particle));
    }

    public static RenderGlobalExt get(RenderGlobal renderGlobal) {
        return ((RenderGlobalExt) ((Object) renderGlobal));
    }
}
