package io.github.derringersmods.toomanyglyphs.common.mixin;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.validation.AugmentCompatibilityValidator;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AugmentAmplifyThree;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AugmentAmplifyTwo;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = AugmentCompatibilityValidator.class, remap = false)
public class AugmentCompatibilityValidatorMixin {
    @Redirect(method = "*",
            at = @At(value = "FIELD",
                    target = "Lcom/hollingsworth/arsnouveau/api/spell/AbstractSpellPart;compatibleAugments",
                    opcode = Opcodes.GETFIELD))
    private static Set<AbstractAugment> adjustCompatibleAugments(@Nonnull AbstractSpellPart instance) {
        Set<AbstractAugment> compatibleAugments = new HashSet<>(instance.compatibleAugments);
        if (compatibleAugments.contains(AugmentAmplify.INSTANCE)) {
            compatibleAugments.add(AugmentAmplifyTwo.INSTANCE);
            compatibleAugments.add(AugmentAmplifyThree.INSTANCE);
        }
        return Collections.unmodifiableSet(compatibleAugments);
    }
}
