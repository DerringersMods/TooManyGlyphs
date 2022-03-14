package io.github.derringersmods.toomanyglyphs.init;

import com.hollingsworth.arsnouveau.api.RegistryHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber
public class TooManyGlyphsConfig {

    public static void registerGlyphConfigs(){
        //RegistryHelper.generateConfig(TooManyGlyphsMod.MODID, ArsNouveauRegistry.registeredSpells);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) { }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) { }
}
