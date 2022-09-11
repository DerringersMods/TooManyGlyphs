package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import io.github.derringersmods.toomanyglyphs.common.network.PacketRayEffect;
import io.github.derringersmods.toomanyglyphs.init.TooManyGlyphsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class MethodRay extends AbstractCastMethod {
    public static final MethodRay INSTANCE = new MethodRay(new ResourceLocation(TooManyGlyphsMod.MODID, "glyph_ray"), "Ray");

    public MethodRay(ResourceLocation tag, String description) {
        super(tag, description);
    }

    double getRange(SpellStats stats) {
        return BASE_RANGE.get() + BONUS_RANGE_PER_AUGMENT.get() * stats.getBuffCount(AugmentAOE.INSTANCE);
    }

    public ForgeConfigSpec.DoubleValue BASE_RANGE;
    public ForgeConfigSpec.DoubleValue BONUS_RANGE_PER_AUGMENT;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        BASE_RANGE = builder.comment("Base range in blocks").defineInRange("base_range", 16d, 0d, Double.MAX_VALUE);
        BONUS_RANGE_PER_AUGMENT = builder.comment("Bonus range per augment").defineInRange("bonus_range_per_augment", 16d, 0d, Double.MAX_VALUE);
    }

    public CastResolveType fireRay(Level world, LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        int sensitivity = stats.getBuffCount(AugmentSensitive.INSTANCE);
        double range = getRange(stats);

        Vec3 fromPoint = shooter.getEyePosition(1.0f);
        Vec3 toPoint = fromPoint.add(shooter.getViewVector(1.0f).scale(range));
        ClipContext rayTraceContext = new ClipContext(fromPoint, toPoint, sensitivity >= 1 ? ClipContext.Block.OUTLINE : ClipContext.Block.COLLIDER, sensitivity >= 2 ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, shooter);
        BlockHitResult blockTarget = world.clip(rayTraceContext);

        if (blockTarget.getType() != HitResult.Type.MISS) {
            BlockPos pos = blockTarget.getBlockPos();
            Vec3 blockCenter = new Vec3(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
            double distance = fromPoint.distanceTo(blockCenter) + 0.5d;
            toPoint = fromPoint.add(shooter.getViewVector(1.0f).scale(Math.min(range, distance)));
        }
        EntityHitResult entityTarget = ProjectileUtil.getEntityHitResult(world, shooter, fromPoint, toPoint, new AABB(fromPoint, toPoint).inflate(1.5d), e -> e != shooter && e.isAlive() && e instanceof LivingEntity);

        if (entityTarget != null)
        {
            resolver.expendMana();
            resolver.onResolveEffect(world, entityTarget);
            Vec3 hitPoint = findNearestPointOnLine(fromPoint, toPoint, entityTarget.getLocation());
            PacketRayEffect.send(world, spellContext, fromPoint, hitPoint);
            return CastResolveType.SUCCESS;
        }

        if (blockTarget.getType() == HitResult.Type.BLOCK)
        {
            resolver.expendMana();
            resolver.onResolveEffect(world, blockTarget);
            PacketRayEffect.send(world, spellContext, fromPoint, blockTarget.getLocation());
            return CastResolveType.SUCCESS;
        }

        if (blockTarget.getType() == HitResult.Type.MISS && sensitivity >= 2)
        {
            Vec3 approximateNormal = fromPoint.subtract(toPoint).normalize();
            blockTarget = new BlockHitResult(toPoint, Direction.getNearest(approximateNormal.x, approximateNormal.y, approximateNormal.z), new BlockPos(toPoint), true);
            resolver.expendMana();
            resolver.onResolveEffect(world, blockTarget);
            PacketRayEffect.send(world, spellContext, fromPoint, blockTarget.getLocation());
            return CastResolveType.SUCCESS;
        }

        PacketRayEffect.send(world, spellContext, fromPoint, toPoint);
        return CastResolveType.FAILURE;
    }

    @Nonnull
    private static Vec3 findNearestPointOnLine(@Nonnull Vec3 fromPoint, @Nonnull Vec3 toPoint, @Nonnull Vec3 hitPoint)
    {
        // algorithm thanks to https://stackoverflow.com/a/9368901
        Vec3 u = toPoint.subtract(fromPoint);
        Vec3 pq = hitPoint.subtract(fromPoint);
        Vec3 w2 = pq.subtract(u.scale(pq.dot(u) / u.lengthSqr()));
        return hitPoint.subtract(w2);
    }

    @Override
    public CastResolveType onCast(@Nullable ItemStack itemStack, LivingEntity shooter, Level world, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        return fireRay(world, shooter, stats, spellContext, spellResolver);
    }

    @Override
    public CastResolveType onCastOnBlock(UseOnContext itemUseContext, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        return fireRay(itemUseContext.getLevel(), itemUseContext.getPlayer(), stats, spellContext, spellResolver);
    }

    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        return fireRay(shooter.level, shooter, stats, spellContext, spellResolver);
    }

    @Override
    public CastResolveType onCastOnEntity(@Nullable ItemStack itemStack, LivingEntity shooter, Entity target, InteractionHand hand, SpellStats stats, SpellContext spellContext, SpellResolver spellResolver) {
        return fireRay(shooter.level, shooter, stats, spellContext, spellResolver);
    }

    @Override
    public int getDefaultManaCost() {
        return 10;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return setOf(AugmentAOE.INSTANCE, AugmentSensitive.INSTANCE);
    }

    @Override
    protected Map<ResourceLocation, Integer> getDefaultAugmentLimits(Map<ResourceLocation, Integer> result) {
        result.put(AugmentSensitive.INSTANCE.getRegistryName(), 2);
        return result;
    }

}
