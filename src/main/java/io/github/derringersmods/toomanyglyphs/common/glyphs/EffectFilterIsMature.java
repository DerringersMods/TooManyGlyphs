package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterIsMature extends EffectFilterEntity {

    public static final EffectFilterIsMature INSTANCE = new EffectFilterIsMature("filter_is_mature", "Filter: Mature");

    public EffectFilterIsMature(String tag, String description) {
        super(tag, description);
    }

    @Override
    public Tier getTier() {
        return Tier.TWO;
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        if (!(target.getEntity() instanceof AgeableEntity)) return false;
        return !((AgeableEntity)target.getEntity()).isBaby();
    }
}
