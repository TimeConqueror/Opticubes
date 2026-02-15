package io.socol.opticubes.mixins.access;

import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderGlobal.class)
public interface RenderGlobalExt {
    @Invoker
    void callMarkBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean updateImmediately);
}
