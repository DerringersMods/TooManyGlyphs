package io.github.derringersmods.toomanyglyphs.api;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;

import java.util.List;

public class FilterUtil {
    public static ITargetFilter getTargetFilter(SpellContext spellContext, ITargetFilter defaultFilter) {
        return getTargetFilter(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().getSpellSize()), defaultFilter);
    }

    public static ITargetFilter getTargetFilter(Spell spell, ITargetFilter defaultFilter) {
        return getTargetFilter(spell.recipe, defaultFilter);
    }

    public static ITargetFilter getTargetFilter(List<AbstractSpellPart> recipe, ITargetFilter defaultFilter) {
        for (AbstractSpellPart part : recipe) {
            if (part instanceof ITargetFilter) return (ITargetFilter) part;
            if (part instanceof AbstractEffect) break;
        }
        return defaultFilter;
    }
}
