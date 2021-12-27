package io.github.derringersmods.toomanyglyphs.init;

import io.github.derringersmods.toomanyglyphs.common.glyphs.EffectFilterBlock;
import io.github.derringersmods.toomanyglyphs.common.glyphs.EffectFilterEntity;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import io.github.derringersmods.toomanyglyphs.common.glyphs.EffectReverseDirection;
import io.github.derringersmods.toomanyglyphs.common.glyphs.MethodLayOnHands;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs() {
        register(EffectFilterBlock.INSTANCE);
        register(EffectFilterEntity.INSTANCE);
        register(MethodLayOnHands.INSTANCE);
        register(EffectReverseDirection.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart) {
        ArsNouveauAPI.getInstance().registerSpell(spellPart.tag, spellPart);
        registeredSpells.add(spellPart);
    }
}
