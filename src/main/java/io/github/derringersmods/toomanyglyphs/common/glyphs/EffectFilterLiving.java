package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EffectFilterLiving extends AbstractEffectFilter {

    public static final EffectFilterLiving INSTANCE = new EffectFilterLiving("filter_living", "Filter: Living");

    public EffectFilterLiving(String tag, String description) {
        super(tag, description);
    }

    @Override
    public Tier getTier() {
        return Tier.ONE;
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        if (!(target.getEntity() instanceof LivingEntity)) return false;
        LivingEntity e = (LivingEntity) target.getEntity();
        return e.isAlive();
    }
}
