package io.github.derringersmods.toomanyglyphs.init;

import com.hollingsworth.arsnouveau.api.spell.ITurretBehavior;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.block.tile.BasicSpellTurretTile;
import io.github.derringersmods.toomanyglyphs.common.glyphs.*;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;

import static com.hollingsworth.arsnouveau.common.block.BasicSpellTurret.TURRET_BEHAVIOR_MAP;
import static io.github.derringersmods.toomanyglyphs.common.glyphs.MethodRay.fireRay;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs() {
        register(EffectFilterBlock.INSTANCE);
        register(EffectFilterEntity.INSTANCE);
        register(EffectFilterLiving.INSTANCE);
        register(EffectFilterLivingNotMonster.INSTANCE);
        register(EffectFilterLivingNotPlayer.INSTANCE);
        register(EffectFilterMonster.INSTANCE);
        register(EffectFilterPlayer.INSTANCE);
        register(MethodLayOnHands.INSTANCE);
        register(EffectReverseDirection.INSTANCE);
        register(EffectChaining.INSTANCE);
        register(MethodRay.INSTANCE);
        register(EffectFilterItem.INSTANCE);
        register(EffectFilterAnimal.INSTANCE);
        register(EffectFilterIsBaby.INSTANCE);
        register(EffectFilterIsMature.INSTANCE);
        register(AugmentAmplifyTwo.INSTANCE);
        register(AugmentAmplifyThree.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart) {
        ArsNouveauAPI.getInstance().registerSpell(spellPart.getId(), spellPart);
        registeredSpells.add(spellPart);
    }

    /* prototype of Ray turret
    static {
        TURRET_BEHAVIOR_MAP.put(MethodRay.INSTANCE, (spellResolver, basicSpellTurretTile, serverLevel, blockPos, fakePlayer, position, direction) -> {
            SpellStats stats = new SpellStats.Builder()
                    .setAugments(spellResolver.spell.getAugments(0, fakePlayer))
                    .build(spellResolver.castType, null, serverLevel, fakePlayer, spellResolver.spellContext);
            fireRay(serverLevel, fakePlayer, stats ,spellResolver.spellContext, spellResolver);
        });
    }
     */

}
