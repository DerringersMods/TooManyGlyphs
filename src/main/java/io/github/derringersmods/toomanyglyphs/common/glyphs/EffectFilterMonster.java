package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterMonster extends EffectFilterLiving {
    public static final EffectFilterMonster INSTANCE = new EffectFilterMonster("filter_monster", "Filter: Monster");

    public EffectFilterMonster(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityHitResult target) {
        return super.matches(target) && target.getEntity().getClassification(false) == MobCategory.MONSTER;
    }
}
