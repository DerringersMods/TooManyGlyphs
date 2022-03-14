package io.github.derringersmods.toomanyglyphs.api.filter;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nonnull;

public interface ITargetFilter {
    boolean matches(BlockHitResult target);
    boolean matches(EntityHitResult target);
}
