package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.common.entity.EntityDummy;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;

public class EffectFilterLivingNotPlayer extends EffectFilterLiving {
    public static final EffectFilterLivingNotPlayer INSTANCE = new EffectFilterLivingNotPlayer("filter_living_not_player", "Filter: Not Player");

    public EffectFilterLivingNotPlayer(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean matches(EntityRayTraceResult target) {
        return super.matches(target) && !(target.getEntity() instanceof PlayerEntity || target.getEntity() instanceof EntityDummy);
    }
}
