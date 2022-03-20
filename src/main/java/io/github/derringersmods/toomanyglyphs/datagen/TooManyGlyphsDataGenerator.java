package io.github.derringersmods.toomanyglyphs.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TooManyGlyphsDataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        System.out.println("Foofoo!");
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new GlyphRecipeProvider(generator));
    }

}


