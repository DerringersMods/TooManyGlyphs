package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterMonster extends EffectFilterLiving {
    public static final EffectFilterMonster INSTANCE = new EffectFilterMonster("filter_monster", "Filter: Monster");

    public EffectFilterMonster(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return super.matches(target) && target.getEntity().getClassification(false) == EntityClassification.MONSTER;
    }
}
