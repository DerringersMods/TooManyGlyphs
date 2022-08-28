package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterLiving extends AbstractEffectFilter {

    public static final EffectFilterLiving INSTANCE = new EffectFilterLiving("filter_living", "Filter: Living");

    public EffectFilterLiving(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target) {
        if (!(target.getEntity() instanceof LivingEntity)) return false;
        return target.getEntity().isAlive();
    }
}
