package io.github.derringersmods.toomanyglyphs.common.mixin;

import com.hollingsworth.arsnouveau.common.block.RuneBlock;
import com.hollingsworth.arsnouveau.common.block.tile.RuneTile;
import io.github.derringersmods.toomanyglyphs.api.FilterUtil;
import io.github.derringersmods.toomanyglyphs.common.glyphs.EffectFilterAny;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.EntityRayTraceResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nonnull;

@Mixin(RuneBlock.class)
public class RuneBlockFilterMixin {
    @Redirect(method = "entityInside",
              at = @At(value = "FIELD",
                       target = "Lcom/hollingsworth/arsnouveau/common/block/tile/RuneTile;touchedEntity:Lnet/minecraft/entity/Entity;",
                       opcode = Opcodes.PUTFIELD))
    private void entityInsideFilterCheck(@Nonnull RuneTile instance, Entity value) {
        if (instance.touchedEntity != null) return;
        if (!FilterUtil.getTargetFilter(instance.spell, EffectFilterAny.INSTANCE).matches(new EntityRayTraceResult(value))) return;
        instance.touchedEntity = value;
    }
}
