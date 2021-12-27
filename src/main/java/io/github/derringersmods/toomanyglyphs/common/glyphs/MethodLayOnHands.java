package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Abs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MethodLayOnHands extends AbstractCastMethod {

    public static final MethodLayOnHands INSTANCE = new MethodLayOnHands("lay_on_hands", "Touch Buff");

    public MethodLayOnHands(String tag, String description) {
        super(tag, description);
    }

    @Override
    public Tier getTier() {
        return Tier.ONE;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    public ITargetFilter getTargetFilter(SpellContext spellContext)
    {
        int index = spellContext.getCurrentIndex();
        Spell spell = spellContext.getSpell();
        for (AbstractSpellPart part : spell.recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().getSpellSize())) {
            if (part instanceof ITargetFilter) return (ITargetFilter) part;
            if (part instanceof AbstractEffect) break;
        }
        return EffectFilterLivingNotMonster.INSTANCE;
    }

    @Override
    public void onCast(@Nullable ItemStack itemStack, LivingEntity caster, World world, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(new EntityRayTraceResult(caster)))
            MethodSelf.INSTANCE.onCast(itemStack, caster, world, augments, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public void onCastOnBlock(ItemUseContext itemUseContext, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(itemUseContext.getHitResult()))
            MethodTouch.INSTANCE.onCastOnBlock(itemUseContext, augments, spellContext, spellResolver);
        else if (itemUseContext.getPlayer() != null && getTargetFilter(spellContext).matches(new EntityRayTraceResult(itemUseContext.getPlayer())))
            MethodSelf.INSTANCE.onCastOnBlock(itemUseContext, augments, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public void onCastOnBlock(BlockRayTraceResult blockRayTraceResult, LivingEntity caster, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(blockRayTraceResult))
            MethodTouch.INSTANCE.onCastOnBlock(blockRayTraceResult, caster, augments, spellContext, spellResolver);
        else if (getTargetFilter(spellContext).matches(new EntityRayTraceResult(caster)))
            MethodSelf.INSTANCE.onCastOnBlock(blockRayTraceResult, caster,augments, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public void onCastOnEntity(@Nullable ItemStack itemStack, LivingEntity caster, Entity target, Hand hand, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        if (getTargetFilter(spellContext).matches(new EntityRayTraceResult(target)))
            MethodTouch.INSTANCE.onCastOnEntity(itemStack, caster, target, hand, augments, spellContext, spellResolver);
        else if (getTargetFilter(spellContext).matches(new EntityRayTraceResult(caster)))
            MethodSelf.INSTANCE.onCastOnEntity(itemStack, caster, target, hand, augments, spellContext, spellResolver);
        else
            spellContext.setCanceled(true);
    }

    @Override
    public boolean wouldCastSuccessfully(@Nullable ItemStack itemStack, LivingEntity livingEntity, World world, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(ItemUseContext itemUseContext, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(BlockRayTraceResult blockRayTraceResult, LivingEntity livingEntity, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnEntitySuccessfully(@Nullable ItemStack itemStack, LivingEntity livingEntity, Entity entity, Hand hand, List<AbstractAugment> list, SpellResolver spellResolver) {
        return true;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }
}
