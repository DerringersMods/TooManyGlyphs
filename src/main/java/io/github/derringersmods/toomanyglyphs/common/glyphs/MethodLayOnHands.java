package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import io.github.derringersmods.toomanyglyphs.api.FilterUtil;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class MethodLayOnHands extends AbstractCastMethod {

    public static final MethodLayOnHands INSTANCE = new MethodLayOnHands(new ResourceLocation(TooManyGlyphsMod.MODID, "glyph_lay_on_hands"), "Lay on Hands");

    public MethodLayOnHands(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

    @Override
    public int getDefaultManaCost() {
        return 10;
    }

    public static ITargetFilter getTargetFilter(SpellContext spellContext)
    {
        return FilterUtil.getTargetFilter(spellContext, EffectFilterLivingNotMonster.INSTANCE);
    }

    @Override
    public CastResolveType onCast(@Nullable ItemStack itemStack, LivingEntity caster, Level world, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(new EntityHitResult(caster)))
            return MethodSelf.INSTANCE.onCast(itemStack, caster, world, stats, spellContext, spellResolver);

        spellContext.setCanceled(true);
        return CastResolveType.FAILURE;
    }

    @Override
    public CastResolveType onCastOnBlock(UseOnContext itemUseContext, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(itemUseContext.getHitResult()))
            return MethodTouch.INSTANCE.onCastOnBlock(itemUseContext, stats, spellContext, spellResolver);
        else if (itemUseContext.getPlayer() != null && getTargetFilter(spellContext).matches(new EntityHitResult(itemUseContext.getPlayer())))
            return MethodSelf.INSTANCE.onCastOnBlock(itemUseContext, stats, spellContext, spellResolver);

        spellContext.setCanceled(true);
        return CastResolveType.FAILURE;
    }

    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(blockRayTraceResult))
            return MethodTouch.INSTANCE.onCastOnBlock(blockRayTraceResult, caster, stats, spellContext, spellResolver);
        else if (getTargetFilter(spellContext).matches(new EntityHitResult(caster)))
            return MethodSelf.INSTANCE.onCastOnBlock(blockRayTraceResult, caster,stats, spellContext, spellResolver);

        spellContext.setCanceled(true);
        return CastResolveType.FAILURE;
    }

    @Override
    public CastResolveType onCastOnEntity(@Nullable ItemStack itemStack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(new EntityHitResult(target)))
            return MethodTouch.INSTANCE.onCastOnEntity(itemStack, caster, target, hand, stats, spellContext, spellResolver);
        else if (getTargetFilter(spellContext).matches(new EntityHitResult(caster)))
            return MethodSelf.INSTANCE.onCastOnEntity(itemStack, caster, target, hand, stats, spellContext, spellResolver);

        spellContext.setCanceled(true);
        return CastResolveType.FAILURE;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }
}
