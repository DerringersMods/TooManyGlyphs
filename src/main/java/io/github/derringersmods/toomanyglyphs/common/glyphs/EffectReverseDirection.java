package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class EffectReverseDirection extends AbstractEffect {
    public static final EffectReverseDirection INSTANCE = new EffectReverseDirection("reverse_direction", "Reverse Direction");

    public EffectReverseDirection(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveBlock(BlockRayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        BlockRayTraceResult reversedRayTraceResult = rayTraceResult
                .withPosition(rayTraceResult.isInside()
                        ? rayTraceResult.getBlockPos()
                        : rayTraceResult.getBlockPos().relative(rayTraceResult.getDirection()).relative(rayTraceResult.getDirection()))
                            // Relative adjustment of 2 required to get to the opposite side of the pivot block
                .withDirection(rayTraceResult.getDirection().getOpposite());
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell continuation = new Spell(new ArrayList<>(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().getSpellSize())));
        SpellContext newContext = new SpellContext(continuation, shooter).withColors(spellContext.colors);
        SpellResolver.resolveEffects(world, shooter, reversedRayTraceResult, continuation, newContext);
    }

    @Override
    public Tier getTier() { return Tier.ONE; }

    @Override
    public int getManaCost() { return 0; }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }
}
