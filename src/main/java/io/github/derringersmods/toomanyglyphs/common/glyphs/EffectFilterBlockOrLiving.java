package io.github.derringersmods.toomanyglyphs.common.glyphs;

import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterBlockOrLiving extends AbstractEffectFilter implements ITargetFilter {

    public static final EffectFilterBlockOrLiving INSTANCE = new EffectFilterBlockOrLiving("filter_block", "Filter: Block");

    public EffectFilterBlockOrLiving(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        if (!(target.getEntity() instanceof LivingEntity)) return false;
        return target.getEntity().isAlive();
    }

    @Override
    public boolean matches(BlockRayTraceResult target) {
        return true;
    }
}
