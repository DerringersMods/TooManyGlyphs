package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterAnimal extends EffectFilterLiving {
    public static EffectFilterAnimal INSTANCE = new EffectFilterAnimal("filter_animal", "Filter: Animal");

    public EffectFilterAnimal(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target) {
        return super.shouldResolveOnEntity(target) && target.getEntity() instanceof Animal;
    }
}
