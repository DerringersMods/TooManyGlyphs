package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class EffectChaining extends AbstractEffect {

    public static final EffectChaining INSTANCE = new EffectChaining("chaining", "Glyph of Chaining");

    public EffectChaining(String tag, String description) {
        super(tag, description);
    }

    // configurable bits
    public ForgeConfigSpec.IntValue BASE_MAX_BLOCKS;
    public ForgeConfigSpec.IntValue BONUS_BLOCKS;
    public ForgeConfigSpec.IntValue BASE_BLOCK_DISTANCE;
    public ForgeConfigSpec.IntValue BONUS_BLOCK_DISTANCE;
    public ForgeConfigSpec.IntValue BASE_MAX_ENTITIES;
    public ForgeConfigSpec.IntValue BONUS_ENTITIES;
    public ForgeConfigSpec.DoubleValue BASE_ENTITY_DISTANCE;
    public ForgeConfigSpec.DoubleValue BONUS_ENTITY_DISTANCE;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        BASE_MAX_BLOCKS = builder.comment("Base maximum number of blocks struck when targeting blocks").defineInRange("base_max_blocks", 64, 1, Integer.MAX_VALUE);
        BONUS_BLOCKS = builder.comment("Bonus to maximum blocks per augment").defineInRange("bonus_blocks", 128, 1, Integer.MAX_VALUE);
        BASE_BLOCK_DISTANCE = builder.comment("Base search distance around each target block").defineInRange("base_block_search_distance", 1, 1, Integer.MAX_VALUE);
        BONUS_BLOCK_DISTANCE = builder.comment("Bonus search distance around each target block per augment").defineInRange("bonus_block_distance", 1, 1, Integer.MAX_VALUE);
        BASE_MAX_ENTITIES = builder.comment("Base maximum number of entities struck when targeting entities").defineInRange("base_max_entities", 8, 1, Integer.MAX_VALUE);
        BONUS_ENTITIES = builder.comment("Bonus to maximum entities per augment").defineInRange("bonus_entities", 16, 1, Integer.MAX_VALUE);
        BASE_ENTITY_DISTANCE = builder.comment("Base search distance around each target entity").defineInRange("base_entity_distance", 4.0d, 0, Double.MAX_VALUE);
        BONUS_ENTITY_DISTANCE = builder.comment("Bonus search distance around each target entity per augment").defineInRange("bonus_entity_distance", 4.0d, 0, Double.MAX_VALUE);
    }

    @Override
    public void onResolveBlock(BlockRayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        int maxBlocks = BASE_MAX_BLOCKS.get() + BONUS_BLOCKS.get() * spellStats.getBuffCount(AugmentAOE.INSTANCE);
        int distance = BASE_BLOCK_DISTANCE.get() + BONUS_BLOCK_DISTANCE.get() * spellStats.getBuffCount(AugmentPierce.INSTANCE);
        BlockState struck = world.getBlockState(rayTraceResult.getBlockPos());
        Iterable<BlockPos> chain = SearchBlockStates(world, Collections.singleton(rayTraceResult.getBlockPos()), maxBlocks, distance, blockState -> blockState.is(struck.getBlock()));
        spellContext.setCanceled(true);
        Spell continuation = new Spell(new ArrayList<>(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().getSpellSize())));
        SpellContext newContext = new SpellContext(continuation, shooter).withColors(spellContext.colors);
        for (BlockPos target : chain)
        {
            Vector3d target3d = new Vector3d(target.getX() + 0.5d, target.getY() + 0.5d, target.getZ() + 0.5d);
            BlockRayTraceResult chainedRayTraceResult = new BlockRayTraceResult(target3d, rayTraceResult.getDirection(), target,true);

            // TODO: better visuals
            ParticleUtil.spawnTouchPacket(world, target, spellContext.colors);
            SpellResolver.resolveEffects(world, shooter, chainedRayTraceResult, continuation, newContext);
        }
    }

    @Override
    public void onResolveEntity(EntityRayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        int maxEntities = BASE_MAX_ENTITIES.get() + BONUS_ENTITIES.get() * spellStats.getBuffCount(AugmentAOE.INSTANCE);
        double distance = BASE_ENTITY_DISTANCE.get() + BONUS_ENTITY_DISTANCE.get() * spellStats.getBuffCount(AugmentPierce.INSTANCE);
        Entity struck = rayTraceResult.getEntity();
        Iterable<Entity> chain = SearchEntities(world, Collections.singleton(struck), maxEntities, distance, e -> e != shooter & e instanceof LivingEntity && e.isAlive());
        spellContext.setCanceled(true);
        Spell continuation = new Spell(new ArrayList<>(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().getSpellSize())));
        SpellContext newContext = new SpellContext(continuation, shooter).withColors(spellContext.colors);
        for (Entity target : chain)
        {
            // TODO: better visuals
            ParticleUtil.spawnTouchPacket(world, target.blockPosition(), spellContext.colors);
            SpellResolver.resolveEffects(world, shooter, new EntityRayTraceResult(target), continuation, newContext);
        }
    }

    @Override
    public Tier getTier() {
        return Tier.TWO;
    }

    @Override
    public int getManaCost() {
        return 150;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return setOf(AugmentAOE.INSTANCE, AugmentPierce.INSTANCE, AugmentSensitive.INSTANCE);
    }

    public static Iterable<BlockPos> SearchBlockStates(World world, Collection<BlockPos> start, int maxBlocks, int searchDistance, Predicate<BlockState> isMatch)
    {
        LinkedList<BlockPos> searchQueue = new LinkedList<>(start);
        HashSet<BlockPos> searched = new HashSet<>(start);
        ArrayList<BlockPos> found = new ArrayList<>();

        while(!searchQueue.isEmpty() && found.size() < maxBlocks) {
            BlockPos current = searchQueue.removeFirst();
            BlockState state = world.getBlockState(current);
            if (isMatch.test(state)) {
                found.add(current);
                BlockPos.betweenClosedStream(current.offset(searchDistance, searchDistance, searchDistance), current.offset(-searchDistance, -searchDistance, -searchDistance)).forEach(neighborMutable -> {
                    if (searched.contains(neighborMutable)) return;
                    BlockPos neighbor = neighborMutable.immutable();
                    searched.add(neighbor);
                    searchQueue.add(neighbor);
                });
            }
        }
        return found;
    }

    public static class Edge<T> {
        public double distanceSqr;
        public T from;
        public T to;

        public Edge(double distanceSqr, T from, T to) {
            this.distanceSqr = distanceSqr;
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge<?> edge = (Edge<?>) o;
            return Double.compare(edge.distanceSqr, distanceSqr) == 0 && Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(distanceSqr, from, to);
        }
    }

    public static Iterable<Entity> SearchEntities(World world, Collection<Entity> start, int maxEntities, double searchDistance, Predicate<Entity> isMatch)
    {
        HashMap<Entity, Edge<Entity>> bestEdgeForTo = new HashMap<>();
        PriorityQueue<Edge<Entity>> searchQueue = new PriorityQueue<>(Comparator.comparingDouble(item -> item.distanceSqr));
        HashSet<Entity> selected = new HashSet<>();
        double searchDistanceSqr = searchDistance * searchDistance;

        start.stream().filter(isMatch).map(e -> new Edge<>(0d, e, e)).forEach(searchQueue::add);

        while (!searchQueue.isEmpty() && selected.size() < maxEntities)
        {
            Edge<Entity> currentEdge = searchQueue.poll();
            Entity current = currentEdge.to;
            selected.add(current);
            Vector3d position = current.position();
            List<Entity> neighbors = world.getEntitiesOfClass(
                    Entity.class,
                    new AxisAlignedBB(position.x + searchDistance, position.y + searchDistance, position.z + searchDistance, position.x - searchDistance, position.y - searchDistance, position.z - searchDistance),
                    e -> e.position().distanceToSqr(current.position()) <= searchDistanceSqr && isMatch.test(e) && !selected.contains(e));
            for (Entity neighbor : neighbors) {
                double distanceSqr = neighbor.position().distanceToSqr(current.position());
                Edge<Entity> bestKnown = bestEdgeForTo.get(neighbor);
                if (bestKnown == null || bestKnown.distanceSqr > distanceSqr) {
                    Edge<Entity> toAdd = new Edge<>(distanceSqr, current, neighbor);
                    if (bestKnown != null) searchQueue.remove(bestKnown);
                    searchQueue.add(toAdd);
                    bestEdgeForTo.put(neighbor, toAdd);
                }
            }
        }

        return selected;
    }
}
