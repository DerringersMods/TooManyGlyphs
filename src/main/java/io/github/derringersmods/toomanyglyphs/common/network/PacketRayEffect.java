package io.github.derringersmods.toomanyglyphs.common.network;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PacketRayEffect {
    public Vector3d from;
    public Vector3d to;
    public ParticleColor.IntWrapper colors;

    public PacketRayEffect(Vector3d from, Vector3d to, ParticleColor.IntWrapper colors) {
        this.from = from;
        this.to = to;
        this.colors = colors;
    }

    public static void encode(PacketRayEffect msg, PacketBuffer buf) {
        NetworkUtil.encode(buf, msg.from);
        NetworkUtil.encode(buf, msg.to);
        NetworkUtil.encode(buf, msg.colors);
    }

    public static PacketRayEffect decode(PacketBuffer buf) {
        Vector3d from = NetworkUtil.decodeVector3d(buf);
        Vector3d to = NetworkUtil.decodeVector3d(buf);
        ParticleColor.IntWrapper colors = NetworkUtil.decodeParticleColorIntWrapper(buf);
        return new PacketRayEffect(from, to, colors);
    }

    public static void handle(final PacketRayEffect msg, Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context ctx = contextSupplier.get();
        if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            ctx.enqueueWork(() -> NetworkUtil.getClientHandlerFor(PacketRayEffect.class).accept(msg, ctx));
        }
        ctx.setPacketHandled(true);
    }

    public static class ClientHandler extends AbstractPacketHandler<PacketRayEffect> {
        public void accept(PacketRayEffect msg, NetworkEvent.Context context) {
            Minecraft mc = Minecraft.getInstance();
            ClientWorld level = mc.level;
            if (level == null) return;
            ClientPlayerEntity player = mc.player;
            if (player == null) return;

            double distance = msg.from.distanceTo(msg.to);
            double start = 0.0, increment = 1.0/16.0;
            if (player.position().distanceToSqr(msg.from) < 4.0 && msg.to.subtract(msg.from).normalize().dot(player.getViewVector(1f)) > MathHelper.SQRT_OF_TWO/2) {
                start = Math.min(2.0, distance / 2.0);
                increment = 1.0 / 8.0;
            }
            for (double d = start; d < distance; d += increment) {
                double fractionalDistance = d / distance;
                double speedCoefficient = MathHelper.lerp(fractionalDistance, 0.2, 0.001);
                level.addParticle(
                        GlowParticleData.createData(msg.colors.toParticleColor()),
                        MathHelper.lerp(fractionalDistance, msg.from.x, msg.to.x),
                        MathHelper.lerp(fractionalDistance, msg.from.y, msg.to.y),
                        MathHelper.lerp(fractionalDistance, msg.from.z, msg.to.z),
                        (level.random.nextFloat() - 0.5) * speedCoefficient,
                        (level.random.nextFloat() - 0.5) * speedCoefficient,
                        (level.random.nextFloat() - 0.5) * speedCoefficient);
            }
        }

    }

    public static void send(@Nonnull World world, @Nonnull SpellContext spellContext, @Nonnull Vector3d fromPoint, @Nonnull Vector3d hitPoint) {
        Vector3d midpoint = fromPoint.add(hitPoint).scale(0.5);
        double radius = 64.0 + fromPoint.distanceTo(midpoint);
        double radiusSqr = radius * radius;

        if (world instanceof ServerWorld)
        {
            PacketRayEffect fx = new PacketRayEffect(fromPoint, hitPoint, spellContext.colors);
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.getChunkSource().chunkMap.getPlayers(new ChunkPos(new BlockPos(midpoint)), false)
                    .filter(p -> p.distanceToSqr(midpoint) <= radiusSqr)
                    .forEach(p -> TooManyGlyphsNetworking.fxChannel.send(PacketDistributor.PLAYER.with(() -> p), fx));
        }
    }
}
