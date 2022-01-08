package io.github.derringersmods.toomanyglyphs.common.network;

import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;

public class NetworkUtil {
    public static void encode(PacketBuffer buf, Vector3d item)
    {
        buf.writeDouble(item.x);
        buf.writeDouble(item.y);
        buf.writeDouble(item.z);
    }

    public static Vector3d decodeVector3d(PacketBuffer buf)
    {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vector3d(x, y, z);
    }

    public static void encode(PacketBuffer buf, ParticleColor.IntWrapper item)
    {
        buf.writeInt((item.r << 16) | (item.g << 8) | item.b);
    }

    public static ParticleColor.IntWrapper decodeParticleColorIntWrapper(PacketBuffer buf)
    {
        int rgb = buf.readInt();
        return new ParticleColor.IntWrapper((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }
}
