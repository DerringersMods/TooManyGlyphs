package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterAnimal extends EffectFilterLiving {
    public static EffectFilterAnimal INSTANCE = new EffectFilterAnimal("filter_animal", "Filter: Animal");

    public EffectFilterAnimal(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return super.matches(target) && target.getEntity() instanceof AnimalEntity;
    }
}
