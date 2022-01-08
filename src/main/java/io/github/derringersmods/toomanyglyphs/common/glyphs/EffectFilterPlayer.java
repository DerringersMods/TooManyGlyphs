package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.common.entity.EntityDummy;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterPlayer extends EffectFilterLiving {
    public static final EffectFilterPlayer INSTANCE = new EffectFilterPlayer("filter_player", "Filter: Player");

    public EffectFilterPlayer(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return super.matches(target) && (target.getEntity() instanceof PlayerEntity || target.getEntity() instanceof EntityDummy);
    }
}
