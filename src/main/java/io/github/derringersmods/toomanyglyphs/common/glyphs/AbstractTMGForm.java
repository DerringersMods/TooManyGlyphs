package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTMGForm extends AbstractCastMethod {
    public AbstractTMGForm(String tag, String description) {
        super(new ResourceLocation(TooManyGlyphsMod.MODID, "glyph_" + tag), description);
    }
}
