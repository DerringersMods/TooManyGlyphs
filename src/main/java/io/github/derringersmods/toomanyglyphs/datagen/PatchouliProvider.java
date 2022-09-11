package io.github.derringersmods.toomanyglyphs.datagen;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.GlyphScribePage;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.PatchouliBuilder;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.TextPage;
import io.github.derringersmods.toomanyglyphs.init.ArsNouveauRegistry;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;

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
    public void addGlyphPage(AbstractSpellPart spellPart) {
        ResourceLocation category = new ResourceLocation(ArsNouveau.MODID, "glyphs_" + spellPart.getTier().value);
        PatchouliBuilder builder = new PatchouliBuilder(category, spellPart.getName())
                .withName(spellPart.getRegistryName().getNamespace() + ".glyph_name." + spellPart.getRegistryName().getPath())
                .withIcon(spellPart.getRegistryName().toString())
                .withSortNum(spellPart instanceof AbstractCastMethod ? 1 : spellPart instanceof AbstractEffect ? 2 : 3)
                .withPage(new TextPage(spellPart.getRegistryName().getNamespace() + ".glyph_desc." + spellPart.getRegistryName().getPath()))
                .withPage(new GlyphScribePage(spellPart));
        this.pages.add(new PatchouliPage(builder, getPath(category, spellPart.getRegistryName().getPath())));
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        for (AbstractSpellPart part : ArsNouveauRegistry.registeredSpells) this.addGlyphPage(part);
        for (PatchouliPage page : pages) DataProvider.saveStable(cache, page.build(), page.path());
    }
}
