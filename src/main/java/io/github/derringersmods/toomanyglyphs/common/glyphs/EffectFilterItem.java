package io.github.derringersmods.toomanyglyphs.common.glyphs;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterItem extends EffectFilterEntity {
    public static final EffectFilterItem INSTANCE = new EffectFilterItem("filter_item", "Filter: Item");

    public EffectFilterItem(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return super.matches(target) && target.getEntity() instanceof ItemEntity;
    }
}
