package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;

public class AugmentAmplifyTwo extends AbstractAugment {

    public static final AugmentAmplifyTwo INSTANCE = new AugmentAmplifyTwo("amplify_two", "Amplify II");

    public AugmentAmplifyTwo(String tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAmplify.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(2.0d);
        return super.applyModifiers(builder, spellPart);
    }
}
