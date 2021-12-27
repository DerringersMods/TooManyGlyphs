package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import io.github.derringersmods.toomanyglyphs.api.filter.ITargetFilter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class EffectFilterBlock extends AbstractEffectFilter implements ITargetFilter {

    public static final EffectFilterBlock INSTANCE = new EffectFilterBlock("filter_block", "Filter: Block");

    public EffectFilterBlock(String tag, String description) {
        super(tag, description);
    }

    @Override
    public Tier getTier() {
        return Tier.ONE;
    }

    @Override
    public boolean matches(BlockRayTraceResult target) {
        return true;
    }
}
