package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractEffectFilter extends AbstractEffect implements ITargetFilter {
    public AbstractEffectFilter(String tag, String description) {
        super(new ResourceLocation(TooManyGlyphsMod.MODID, "glyph_" + tag) , description);
    }

    public AbstractEffectFilter(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public String getLocaleName() {
        return super.getLocaleName();
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver spellResolver) {
        if (!matches(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver spellResolver) {
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
}
