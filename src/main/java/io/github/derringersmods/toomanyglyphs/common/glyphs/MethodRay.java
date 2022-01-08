package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class MethodRay extends AbstractCastMethod {
    public static final MethodRay INSTANCE = new MethodRay("ray", "Ray");

    public MethodRay(String tag, String description) {
        super(tag, description);
    }

    double getRange(List<AbstractAugment> augments) {
        return BASE_RANGE.get() + BONUS_RANGE_PER_AUGMENT.get() * getBuffCount(augments, AugmentAOE.class);
    }

    public ForgeConfigSpec.DoubleValue BASE_RANGE;
    public ForgeConfigSpec.DoubleValue BONUS_RANGE_PER_AUGMENT;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        BASE_RANGE = builder.comment("Base range in blocks").defineInRange("base_range", 16d, 0d, Double.MAX_VALUE);
        BONUS_RANGE_PER_AUGMENT = builder.comment("Bonus range per augment").defineInRange("bonus_range_per_augment", 16d, 0d, Double.MAX_VALUE);
    }

    public void fireRay(World world, LivingEntity shooter, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver resolver) {
        int sensitivity = getBuffCount(augments, AugmentSensitive.class);
        double range = getRange(augments);

        Vector3d fromPoint = shooter.getEyePosition(1.0f);
        Vector3d toPoint = fromPoint.add(shooter.getViewVector(1.0f).scale(range));
        RayTraceContext rayTraceContext = new RayTraceContext(fromPoint, toPoint, sensitivity >= 1 ? RayTraceContext.BlockMode.OUTLINE : RayTraceContext.BlockMode.COLLIDER, sensitivity >= 2 ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, shooter);
        BlockRayTraceResult blockTarget = world.clip(rayTraceContext);

        if (blockTarget.getType() != RayTraceResult.Type.MISS) {
            BlockPos pos = blockTarget.getBlockPos();
            Vector3d blockCenter = new Vector3d(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
            double distance = fromPoint.distanceTo(blockCenter) + 0.5d;
            toPoint = fromPoint.add(shooter.getViewVector(1.0f).scale(Math.min(range, distance)));
        }
        EntityRayTraceResult entityTarget = ProjectileHelper.getEntityHitResult(world, shooter, fromPoint, toPoint, new AxisAlignedBB(fromPoint, toPoint).inflate(1.5d), e -> e != shooter && e.isAlive() && e instanceof LivingEntity);

        if (entityTarget != null)
        {
            resolver.onResolveEffect(world, shooter, entityTarget);
            resolver.expendMana(shooter);
            Networking.sendToNearby(world, shooter, new PacketANEffect(PacketANEffect.EffectType.BURST, entityTarget.getEntity().blockPosition(), spellContext.colors, new int[0]));
            return;
        }

        if (blockTarget.getType() == RayTraceResult.Type.BLOCK)
        {
            resolver.onResolveEffect(world, shooter, blockTarget);
            resolver.expendMana(shooter);
            Networking.sendToNearby(world, shooter, new PacketANEffect(PacketANEffect.EffectType.BURST, blockTarget.getBlockPos(), spellContext.colors, new int[0]));
            return;
        }

        // Fizzle!
        resolver.expendMana(shooter);
    }

    @Override
    public void onCast(@Nullable ItemStack itemStack, LivingEntity shooter, World world, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        fireRay(world, shooter, augments, spellContext, spellResolver);
    }

    @Override
    public void onCastOnBlock(ItemUseContext itemUseContext, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        fireRay(itemUseContext.getLevel(), itemUseContext.getPlayer(), augments, spellContext, spellResolver);
    }

    @Override
    public void onCastOnBlock(BlockRayTraceResult blockRayTraceResult, LivingEntity shooter, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        fireRay(shooter.level, shooter, augments, spellContext, spellResolver);
    }

    @Override
    public void onCastOnEntity(@Nullable ItemStack itemStack, LivingEntity shooter, Entity target, Hand hand, List<AbstractAugment> augments, SpellContext spellContext, SpellResolver spellResolver) {
        fireRay(shooter.level, shooter, augments, spellContext, spellResolver);
    }

    @Override
    public boolean wouldCastSuccessfully(@Nullable ItemStack itemStack, LivingEntity shooter, World world, List<AbstractAugment> list, SpellResolver spellResolver) {
        return true;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(ItemUseContext itemUseContext, List<AbstractAugment> list, SpellResolver spellResolver) {
        return true;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(BlockRayTraceResult blockRayTraceResult, LivingEntity livingEntity, List<AbstractAugment> list, SpellResolver spellResolver) {
        return true;
    }

    @Override
    public boolean wouldCastOnEntitySuccessfully(@Nullable ItemStack itemStack, LivingEntity livingEntity, Entity entity, Hand hand, List<AbstractAugment> list, SpellResolver spellResolver) {
        return true;
    }

    @Override
    public int getManaCost() {
        return 15;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return setOf(AugmentAOE.INSTANCE, AugmentSensitive.INSTANCE);
    }
}