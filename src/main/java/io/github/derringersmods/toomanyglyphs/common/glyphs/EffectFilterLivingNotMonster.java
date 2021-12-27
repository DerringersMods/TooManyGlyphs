package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterLivingNotMonster extends EffectFilterLiving {
    public static final EffectFilterLivingNotMonster INSTANCE = new EffectFilterLivingNotMonster("filter_living_not_monster", "Filter: Not Monster");

    public EffectFilterLivingNotMonster(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return super.matches(target) && target.getEntity().getClassification(false) != EntityClassification.MONSTER;
    }
}
