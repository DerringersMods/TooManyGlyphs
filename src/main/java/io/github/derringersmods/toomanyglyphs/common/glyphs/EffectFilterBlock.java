package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.world.phys.BlockHitResult;

public class EffectFilterBlock extends AbstractEffectFilter implements ITargetFilter {

    public static final EffectFilterBlock INSTANCE = new EffectFilterBlock("filter_block", "Filter: Block");

    public EffectFilterBlock(String tag, String description) {
        super(tag, description);
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

    @Override
    public boolean shouldResolveOnBlock(BlockHitResult target) {
        return true;
    }
}
