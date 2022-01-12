package io.github.derringersmods.toomanyglyphs.common.network;

import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkUtil {
    public static ConcurrentHashMap<Class, AbstractPacketHandler> clientHandlers = new ConcurrentHashMap<>();

    public static <MSG> AbstractPacketHandler<MSG> getClientHandlerFor(Class<MSG> packetType) {
            return (AbstractPacketHandler<MSG>) clientHandlers.computeIfAbsent(packetType, t -> {
                try {
                    return (AbstractPacketHandler<MSG>) Class.forName(t.getTypeName() + "$ClientHandler").newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
                    e.printStackTrace();
                }
                return NullPacketHandler.INSTANCE;
            });
    }

    public static class NullPacketHandler<MSG> extends AbstractPacketHandler<MSG>
    {
        public static final NullPacketHandler<Object> INSTANCE = new NullPacketHandler<>();

        @Override
        public void accept(MSG msg, NetworkEvent.Context context) { }
    }

    public static void encode(@Nonnull PacketBuffer buf, @Nonnull Vector3d item) {
        buf.writeDouble(item.x);
        buf.writeDouble(item.y);
        buf.writeDouble(item.z);
    }

    @Nonnull
    public static Vector3d decodeVector3d(@Nonnull PacketBuffer buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vector3d(x, y, z);
    }

    public static void encode(@Nonnull PacketBuffer buf, @Nonnull ParticleColor.IntWrapper item) {
        buf.writeInt((item.r << 16) | (item.g << 8) | item.b);
    }

    @Nonnull
    public static ParticleColor.IntWrapper decodeParticleColorIntWrapper(@Nonnull PacketBuffer buf) {
        int rgb = buf.readInt();
        return new ParticleColor.IntWrapper((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }
}
