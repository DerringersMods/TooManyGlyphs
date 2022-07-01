package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTMGAugment extends AbstractAugment {

    public AbstractTMGAugment(String tag, String description) {
        super(new ResourceLocation(TooManyGlyphsMod.MODID, "glyph_" + tag), description);
    }

}
