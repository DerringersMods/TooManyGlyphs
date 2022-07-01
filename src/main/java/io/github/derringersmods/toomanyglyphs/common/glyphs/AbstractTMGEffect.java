package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTMGEffect extends AbstractEffect {
    public AbstractTMGEffect(String tag, String description) {
        super(new ResourceLocation(TooManyGlyphsMod.MODID, "glyph_" + tag), description);
    }
}
