package io.github.derringersmods.toomanyglyphs.common.util;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.common.spell.validation.SpellPhraseValidator;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Comparator;

public class SpellPartPositionComparator implements Comparator<SpellPhraseValidator.SpellPhrase.SpellPartPosition<AbstractAugment>> {

    public static final SpellPartPositionComparator INSTANCE = new SpellPartPositionComparator();

    private Field positionField;

    private SpellPartPositionComparator() {
        try {
            positionField = SpellPhraseValidator.SpellPhrase.SpellPartPosition.class.getDeclaredField("position");
            positionField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            positionField = null;
        }
    }


    @Override
    public int compare(@NotNull SpellPhraseValidator.SpellPhrase.SpellPartPosition<AbstractAugment> o1, @NotNull SpellPhraseValidator.SpellPhrase.SpellPartPosition<AbstractAugment> o2) {
        try {
            return positionField.getInt(o1) - positionField.getInt(o2);
        } catch(IllegalAccessException ex) {
            return 0;
        } catch(NullPointerException ex) {
            return 0;
        }
    }
}
