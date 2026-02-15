package io.socol.opticubes.utils;

import io.socol.opticubes.mixins.access.ParticleExt;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPosExt {
    public static BlockPos of(Entity entity) {
        return new BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
    }

    @SideOnly(Side.CLIENT)
    public static BlockPos of(Particle particle) {
        ParticleExt ext = ClientMixinAccessor.get(particle);
        return new BlockPos(MathHelper.floor(ext.getPosX()), MathHelper.floor(ext.getPosY()), MathHelper.floor(ext.getPosZ()));
    }

    public static int chunkCenterX(ChunkPos cPos) {
        return (cPos.x << 4) + 8;
    }

    public static int chunkCenterZ(ChunkPos cPos) {
        return (cPos.z << 4) + 8;
    }
}
