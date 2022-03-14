package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractEffectFilter extends AbstractEffect implements ITargetFilter {
    public AbstractEffectFilter(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        if (!matches(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        if (!matches(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }

    @Override
    public boolean matches(BlockHitResult target) {
        return false;
    }

    @Override
    public boolean matches(EntityHitResult target) {
        return false;
    }

    @Override
    public boolean wouldSucceed(HitResult rayTraceResult, Level level, LivingEntity shooter, SpellStats stats, SpellContext context) {
        if (rayTraceResult == null) return false;
        if (rayTraceResult.getType() == HitResult.Type.MISS) return false;
        if (rayTraceResult instanceof BlockHitResult)
            return matches((BlockHitResult) rayTraceResult);
        if (rayTraceResult instanceof EntityHitResult)
            return matches((EntityHitResult) rayTraceResult);
        return false;
    }
}
