package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;

public class AugmentAmplifyTwo extends AbstractAugment {

    public static final AugmentAmplifyTwo INSTANCE = new AugmentAmplifyTwo("amplify_two", "Amplify II");

    public AugmentAmplifyTwo(String tag, String description) {
        super(tag, description);
    }

    @Override
    public int getManaCost() {
        return AugmentAmplify.INSTANCE.getManaCost() * 4;
    }

    @Override
    public Tier getTier() {
        return Tier.TWO;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(2.0d);
        return super.applyModifiers(builder, spellPart);
    }
}
