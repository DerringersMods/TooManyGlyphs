package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterAny extends AbstractEffectFilter {

    public static final EffectFilterAny INSTANCE = new EffectFilterAny("filter_any", "Filter: Any");

    public EffectFilterAny(String tag, String description) {
        super(tag, description);
    }

    @Override
    public Tier getTier() {
        return Tier.ONE;
    }

    @Override
    public boolean matches(BlockRayTraceResult target) {
        return true;
    }

    @Override
    public boolean matches(EntityRayTraceResult target) { return true; }
}
