package io.github.derringersmods.toomanyglyphs.init;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import io.github.derringersmods.toomanyglyphs.common.glyphs.*;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs() {
        register(EffectFilterBlock.INSTANCE);
        register(EffectFilterEntity.INSTANCE);
        register(EffectFilterLiving.INSTANCE);
        register(EffectFilterLivingNotMonster.INSTANCE);
        register(EffectFilterLivingNotPlayer.INSTANCE);
        register(EffectFilterMonster.INSTANCE);
        register(EffectFilterPlayer.INSTANCE);
        register(MethodLayOnHands.INSTANCE);
        register(EffectReverseDirection.INSTANCE);
        register(EffectChaining.INSTANCE);
        register(MethodRay.INSTANCE);
        register(EffectFilterItem.INSTANCE);
        register(EffectFilterAnimal.INSTANCE);
        register(EffectFilterIsBaby.INSTANCE);
        register(EffectFilterIsMature.INSTANCE);
        register(AugmentAmplifyTwo.INSTANCE);
        register(AugmentAmplifyThree.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart) {
        ArsNouveauAPI.getInstance().registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static void addAugments(){
        for (AbstractSpellPart spell : ArsNouveauAPI.getInstance().getSpellpartMap().values().stream().filter(s -> s instanceof AbstractEffect).toList()) {
            if (spell.compatibleAugments.contains(AugmentAmplify.INSTANCE)){
                spell.compatibleAugments.add(AugmentAmplifyTwo.INSTANCE);
                spell.compatibleAugments.add(AugmentAmplifyThree.INSTANCE);
            }
        }
    }
}
