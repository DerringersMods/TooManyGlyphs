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

public class EffectFilterEntity extends AbstractEffectFilter {

    public static final EffectFilterEntity INSTANCE = new EffectFilterEntity("filter_entity", "Filter: Entity");

    public EffectFilterEntity(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return true;
    }
}
