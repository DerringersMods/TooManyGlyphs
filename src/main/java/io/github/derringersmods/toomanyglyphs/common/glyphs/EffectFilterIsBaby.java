package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterIsBaby extends EffectFilterEntity {

    public static final EffectFilterIsBaby INSTANCE = new EffectFilterIsBaby("filter_is_baby", "Filter: Baby");

    public EffectFilterIsBaby(String tag, String description) {
        super(tag, description);
    }

    @Override
    public Tier getTier() {
        return Tier.TWO;
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        if (!(target.getEntity() instanceof AgeableEntity)) return false;
        return ((AgeableEntity)target.getEntity()).isBaby();
    }
}
