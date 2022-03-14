package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import io.github.derringersmods.toomanyglyphs.api.FilterUtil;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
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
import java.util.List;
import java.util.Set;

public class MethodLayOnHands extends AbstractCastMethod {

    public static final MethodLayOnHands INSTANCE = new MethodLayOnHands("lay_on_hands", "Lay on Hands");

    public MethodLayOnHands(String tag, String description) {
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
    public void onCast(@Nullable ItemStack itemStack, LivingEntity caster, Level world, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(new EntityHitResult(caster)))
            MethodSelf.INSTANCE.onCast(itemStack, caster, world, stats, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public void onCastOnBlock(UseOnContext itemUseContext, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(itemUseContext.getHitResult()))
            MethodTouch.INSTANCE.onCastOnBlock(itemUseContext, stats, spellContext, spellResolver);
        else if (itemUseContext.getPlayer() != null && getTargetFilter(spellContext).matches(new EntityHitResult(itemUseContext.getPlayer())))
            MethodSelf.INSTANCE.onCastOnBlock(itemUseContext, stats, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public void onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(blockRayTraceResult))
            MethodTouch.INSTANCE.onCastOnBlock(blockRayTraceResult, caster, stats, spellContext, spellResolver);
        else if (getTargetFilter(spellContext).matches(new EntityHitResult(caster)))
            MethodSelf.INSTANCE.onCastOnBlock(blockRayTraceResult, caster,stats, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public void onCastOnEntity(@Nullable ItemStack itemStack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(new EntityHitResult(target)))
            MethodTouch.INSTANCE.onCastOnEntity(itemStack, caster, target, hand, stats, spellContext, spellResolver);
        else if (getTargetFilter(spellContext).matches(new EntityHitResult(caster)))
            MethodSelf.INSTANCE.onCastOnEntity(itemStack, caster, target, hand, stats, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public boolean wouldCastSuccessfully(@Nullable ItemStack itemStack, LivingEntity livingEntity, Level world, SpellStats stats, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(UseOnContext itemUseContext, SpellStats stats, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(BlockHitResult blockRayTraceResult, LivingEntity livingEntity, SpellStats stats, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnEntitySuccessfully(@Nullable ItemStack itemStack, LivingEntity livingEntity, Entity entity, InteractionHand hand, SpellStats stats, SpellResolver spellResolver) {
        return true;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }
}
