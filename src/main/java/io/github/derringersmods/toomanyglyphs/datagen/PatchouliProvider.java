package io.github.derringersmods.toomanyglyphs.datagen;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import io.github.derringersmods.toomanyglyphs.init.ArsNouveauRegistry;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

public class PatchouliProvider extends com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider
{
    public PatchouliProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public Path getPath(ResourceLocation category, String fileName) {
        return this.generator.getOutputFolder().resolve("data/" + TooManyGlyphsMod.MODID + "/patchouli_books/worn_notebook/en_us/entries/" + category.getPath() + "/" + fileName + ".json");
    }

    @Override
    public void run(HashCache cache) throws IOException {
        for (AbstractSpellPart part : ArsNouveauRegistry.registeredSpells) this.addGlyphPage(part);
        for (PatchouliPage page : pages) DataProvider.save(GSON, cache, page.build(), page.path());
    }
}
