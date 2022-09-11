package io.github.derringersmods.toomanyglyphs.common.network;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PacketRayEffect {
    public Vec3 from;
    public Vec3 to;
    public ParticleColor color;

    public PacketRayEffect(Vec3 from, Vec3 to, ParticleColor colors) {
        this.from = from;
        this.to = to;
        this.color = colors;
    }

    public static void encode(PacketRayEffect msg, FriendlyByteBuf buf) {
        NetworkUtil.encode(buf, msg.from);
        NetworkUtil.encode(buf, msg.to);
        NetworkUtil.encode(buf, msg.color);
    }

    public static PacketRayEffect decode(FriendlyByteBuf buf) {
        Vec3 from = NetworkUtil.decodeVector3d(buf);
        Vec3 to = NetworkUtil.decodeVector3d(buf);
        ParticleColor color = NetworkUtil.decodeParticleColor(buf);
        return new PacketRayEffect(from, to, color);
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
            ClientLevel level = mc.level;
            if (level == null) return;
            LocalPlayer player = mc.player;
            if (player == null) return;

            double distance = msg.from.distanceTo(msg.to);
            double start = 0.0, increment = 1.0/16.0;
            if (player.position().distanceToSqr(msg.from) < 4.0 && msg.to.subtract(msg.from).normalize().dot(player.getViewVector(1f)) > Mth.SQRT_OF_TWO/2) {
                start = Math.min(2.0, distance / 2.0);
                increment = 1.0 / 8.0;
            }
            for (double d = start; d < distance; d += increment) {
                double fractionalDistance = d / distance;
                double speedCoefficient = Mth.lerp(fractionalDistance, 0.2, 0.001);
                level.addParticle(
                        GlowParticleData.createData(msg.color),
                        Mth.lerp(fractionalDistance, msg.from.x, msg.to.x),
                        Mth.lerp(fractionalDistance, msg.from.y, msg.to.y),
                        Mth.lerp(fractionalDistance, msg.from.z, msg.to.z),
                        (level.random.nextFloat() - 0.5) * speedCoefficient,
                        (level.random.nextFloat() - 0.5) * speedCoefficient,
                        (level.random.nextFloat() - 0.5) * speedCoefficient);
            }
        }

    }

    public static void send(@Nonnull Level level, @Nonnull SpellContext spellContext, @Nonnull Vec3 fromPoint, @Nonnull Vec3 hitPoint) {
        Vec3 midpoint = fromPoint.add(hitPoint).scale(0.5);
        double radius = 64.0 + fromPoint.distanceTo(midpoint);
        double radiusSqr = radius * radius;

        if (level instanceof ServerLevel serverLevel)
        {
            PacketRayEffect fx = new PacketRayEffect(fromPoint, hitPoint, spellContext.getColors());
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(new BlockPos(midpoint)), false)
                    .stream()
                    .filter(p -> p.distanceToSqr(midpoint) <= radiusSqr)
                    .forEach(p -> TooManyGlyphsNetworking.fxChannel.send(PacketDistributor.PLAYER.with(() -> p), fx));
        }
    }
}
