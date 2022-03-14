package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterEntity extends AbstractEffectFilter {

    public static final EffectFilterEntity INSTANCE = new EffectFilterEntity("filter_entity", "Filter: Entity");

    public EffectFilterEntity(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityHitResult target) {
        return true;
    }
}
