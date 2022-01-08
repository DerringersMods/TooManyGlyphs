package io.github.derringersmods.toomanyglyphs.init;

import io.github.derringersmods.toomanyglyphs.common.network.PacketRayEffect;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TooManyGlyphsNetworking {
    public static SimpleChannel fxChannel;

    public static void registerNetwork()
    {
        fxChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(TooManyGlyphsMod.MODID, "fx"), () -> "1", v -> true, v -> true);

        fxChannel.registerMessage(0,
                PacketRayEffect.class,
                PacketRayEffect::encode,
                PacketRayEffect::decode,
                PacketRayEffect::handle);
    }
}
