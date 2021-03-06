package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.EntityHitResult;

public class EffectFilterItem extends EffectFilterEntity {
    public static final EffectFilterItem INSTANCE = new EffectFilterItem("filter_item", "Filter: Item");

    public EffectFilterItem(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityHitResult target) {
        return super.matches(target) && target.getEntity() instanceof ItemEntity;
    }
}
