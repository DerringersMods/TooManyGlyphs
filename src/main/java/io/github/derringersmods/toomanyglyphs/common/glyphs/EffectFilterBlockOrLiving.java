package io.github.derringersmods.toomanyglyphs.common.glyphs;

import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterBlockOrLiving extends AbstractEffectFilter implements ITargetFilter {

    public static final EffectFilterBlockOrLiving INSTANCE = new EffectFilterBlockOrLiving("filter_block", "Filter: Block");

    public EffectFilterBlockOrLiving(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityHitResult target) {
        if (!(target.getEntity() instanceof LivingEntity)) return false;
        return target.getEntity().isAlive();
    }

    @Override
    public boolean matches(BlockHitResult target) {
        return true;
    }
}
