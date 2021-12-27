package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
    public void onResolveEntity(EntityRayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        if (!matches(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Override
    public void onResolveBlock(BlockRayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        if (!matches(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }

    @Override
    public boolean matches(BlockRayTraceResult target) {
        return false;
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return false;
    }

    @Override
    public boolean wouldSucceed(RayTraceResult rayTraceResult, World world, LivingEntity shooter, List<AbstractAugment> augments) {
        if (rayTraceResult == null) return false;
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS) return false;
        if (rayTraceResult instanceof BlockRayTraceResult)
            return matches((BlockRayTraceResult) rayTraceResult);
        if (rayTraceResult instanceof EntityRayTraceResult)
            return matches((EntityRayTraceResult) rayTraceResult);
        return false;
    }
}
