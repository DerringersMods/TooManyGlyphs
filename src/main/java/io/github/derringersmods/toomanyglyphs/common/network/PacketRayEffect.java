package io.github.derringersmods.toomanyglyphs.common.network;

import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
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
            ctx.enqueueWork(new Runnable() {
                @Override
                public void run() {
                    Minecraft mc = Minecraft.getInstance();
                    ClientWorld level = mc.level;

                    double distance = msg.from.distanceTo(msg.to);
                    for (double d = 0.0; d < distance; d += 1/16.0) {
                        double fractionalDistance = d / distance;
                        level.addParticle(
                                GlowParticleData.createData(msg.colors.toParticleColor()),
                                MathHelper.lerp(fractionalDistance, msg.from.x, msg.to.x),
                                MathHelper.lerp(fractionalDistance, msg.from.y, msg.to.y),
                                MathHelper.lerp(fractionalDistance, msg.from.z, msg.to.z),
                                (level.random.nextFloat() - 0.5) * 0.1,
                                (level.random.nextFloat() - 0.5) * 0.1,
                                (level.random.nextFloat() - 0.5) * 0.1);
                    }
                }
            });
        }
        ctx.setPacketHandled(true);
    }

}
