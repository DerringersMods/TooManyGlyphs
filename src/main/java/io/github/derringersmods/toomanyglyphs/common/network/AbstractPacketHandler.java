package io.github.derringersmods.toomanyglyphs.common.network;

import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class AbstractPacketHandler <MSG> implements BiConsumer<MSG, NetworkEvent.Context> {
    public abstract void accept(MSG msg, NetworkEvent.Context context);
}
