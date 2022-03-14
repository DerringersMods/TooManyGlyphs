package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterLivingNotMonster extends EffectFilterLiving {
    public static final EffectFilterLivingNotMonster INSTANCE = new EffectFilterLivingNotMonster("filter_living_not_monster", "Filter: Not Monster");

    public EffectFilterLivingNotMonster(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityHitResult target) {
        return super.matches(target) && target.getEntity().getClassification(false) != MobCategory.MONSTER;
    }
}
