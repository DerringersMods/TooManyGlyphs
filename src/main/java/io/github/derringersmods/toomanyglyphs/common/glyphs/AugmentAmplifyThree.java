package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;


public class AugmentAmplifyThree extends AbstractAugment {

    public static final AugmentAmplifyThree INSTANCE = new AugmentAmplifyThree("amplify_three", "Amplify III");

    public AugmentAmplifyThree(String tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAmplify.INSTANCE.getDefaultManaCost() * 16;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(4.0d);
        return super.applyModifiers(builder, spellPart);
    }
}
