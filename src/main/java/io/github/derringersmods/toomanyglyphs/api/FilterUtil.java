package io.github.derringersmods.toomanyglyphs.api;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import io.github.derringersmods.toomanyglyphs.common.glyphs.EffectFilterLivingNotMonster;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Abs;

import java.util.List;

public class FilterUtil {
    public static ITargetFilter getTargetFilter(SpellContext spellContext, ITargetFilter defaultFilter) {
        return getTargetFilter(spellContext.getRemainingSpell(), defaultFilter);
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
