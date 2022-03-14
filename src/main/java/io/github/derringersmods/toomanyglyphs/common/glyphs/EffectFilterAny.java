package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterAny extends AbstractEffectFilter {

    public static final EffectFilterAny INSTANCE = new EffectFilterAny("filter_any", "Filter: Any");

    public EffectFilterAny(String tag, String description) {
        super(tag, description);
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

    @Override
    public boolean matches(BlockHitResult target) {
        return true;
    }

    @Override
    public boolean matches(EntityHitResult target) { return true; }
}
