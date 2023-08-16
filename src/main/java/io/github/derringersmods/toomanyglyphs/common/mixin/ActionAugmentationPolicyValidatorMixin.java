package io.github.derringersmods.toomanyglyphs.common.mixin;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.validation.ActionAugmentationPolicyValidator;
import com.hollingsworth.arsnouveau.common.spell.validation.SpellPhraseValidator;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AugmentAmplifyThree;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AugmentAmplifyTwo;
import io.github.derringersmods.toomanyglyphs.common.util.SpellPartPositionComparator;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(value = ActionAugmentationPolicyValidator.class, remap = false)
public class ActionAugmentationPolicyValidatorMixin {
    @Redirect(method = "*",
              at = @At(value = "INVOKE",
                       target = "Lcom/hollingsworth/arsnouveau/common/spell/validation/SpellPhraseValidator$SpellPhrase;getAugmentPositionMap()Ljava/util/Map;"))
    private Map<ResourceLocation, List<SpellPhraseValidator.SpellPhrase.SpellPartPosition<AbstractAugment>>> patchedGetAugmentPositionMap(SpellPhraseValidator.SpellPhrase phrase)
    {
        var originalAugmentPositionMap = phrase.getAugmentPositionMap();
        var newMap = new HashMap<>(originalAugmentPositionMap);
        var list = new ArrayList<SpellPhraseValidator.SpellPhrase.SpellPartPosition<AbstractAugment>>();
        list.addAll(originalAugmentPositionMap.getOrDefault(AugmentAmplify.INSTANCE.getRegistryName(), Collections.EMPTY_LIST));
        list.addAll(originalAugmentPositionMap.getOrDefault(AugmentAmplifyTwo.INSTANCE.getRegistryName(), Collections.EMPTY_LIST));
        list.addAll(originalAugmentPositionMap.getOrDefault(AugmentAmplifyThree.INSTANCE.getRegistryName(), Collections.EMPTY_LIST));
        list.sort(SpellPartPositionComparator.INSTANCE);
        newMap.remove(AugmentAmplifyTwo.INSTANCE.getRegistryName());
        newMap.remove(AugmentAmplifyThree.INSTANCE.getRegistryName());
        newMap.put(AugmentAmplify.INSTANCE.getRegistryName(), Collections.unmodifiableList(list));
        return newMap;
    }

}
