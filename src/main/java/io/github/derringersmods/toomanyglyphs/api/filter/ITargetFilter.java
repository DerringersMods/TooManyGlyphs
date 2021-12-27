package io.github.derringersmods.toomanyglyphs.api.filter;

import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

import javax.annotation.Nonnull;

public interface ITargetFilter {
    boolean matches(BlockRayTraceResult target);
    boolean matches(EntityRayTraceResult target);
}
