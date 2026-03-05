package com.example.frontierresearch.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores all research-related data for a single player.
 *
 * <p>This class is serialised to / from NBT and attached to
 * each {@link net.minecraft.world.entity.player.Player} via
 * {@link com.example.frontierresearch.registry.ModAttachments#RESEARCH_DATA}.</p>
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code unlockedTechnologies} – ids of fully researched technologies</li>
 *   <li>{@code activeResearch} – id of the technology currently being researched, or
 *       {@code null} if idle</li>
 *   <li>{@code researchProgress} – consumed science-pack ticks towards the active
 *       research (reset to 0 when technology completes)</li>
 * </ul>
 */
public class PlayerResearchData {

    // ------------------------------------------------------------------
    // Codec for NeoForge attachment serialisation
    // ------------------------------------------------------------------

    /**
     * A DFU {@link Codec} that serialises this class to/from a data-ops structure
     * (typically an NBT {@link CompoundTag}).  Used by
     * {@link com.example.frontierresearch.registry.ModAttachments#RESEARCH_DATA}.
     */
    public static final Codec<PlayerResearchData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf()
                            .fieldOf("unlocked")
                            .forGetter(d -> new ArrayList<>(d.getUnlockedTechnologies())),
                    ResourceLocation.CODEC.optionalFieldOf("active")
                            .forGetter(d -> Optional.ofNullable(d.getActiveResearch())),
                    Codec.INT.fieldOf("progress")
                            .forGetter(PlayerResearchData::getResearchProgress)
            ).apply(instance, (unlocked, active, progress) -> {
                PlayerResearchData data = new PlayerResearchData();
                unlocked.forEach(data::unlock);
                active.ifPresent(id -> {
                    data.setActiveResearch(id);  // resets progress to 0
                    data.addProgress(progress);  // restore saved progress
                });
                return data;
            })
    );

    private static final String TAG_UNLOCKED = "UnlockedTechnologies";
    private static final String TAG_ACTIVE = "ActiveResearch";
    private static final String TAG_PROGRESS = "ResearchProgress";

    private final Set<ResourceLocation> unlockedTechnologies = new LinkedHashSet<>();
    private ResourceLocation activeResearch = null;
    private int researchProgress = 0;

    public PlayerResearchData() {}

    // -----------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------

    /**
     * Returns {@code true} if the given technology id has been fully researched.
     *
     * @param technologyId technology id to check
     * @return {@code true} if unlocked
     */
    public boolean isUnlocked(ResourceLocation technologyId) {
        return unlockedTechnologies.contains(technologyId);
    }

    /**
     * Marks a technology as fully researched.
     *
     * @param technologyId technology id to unlock
     */
    public void unlock(ResourceLocation technologyId) {
        unlockedTechnologies.add(technologyId);
    }

    /**
     * Replaces the entire unlocked-technology set with the provided set.
     * Used when syncing state from the server.
     *
     * @param ids the new set of unlocked technology ids
     */
    public void replaceUnlocked(Set<ResourceLocation> ids) {
        unlockedTechnologies.clear();
        unlockedTechnologies.addAll(ids);
    }

    /**
     * Returns an unmodifiable view of all unlocked technology ids.
     *
     * @return set of unlocked technology ids
     */
    public Set<ResourceLocation> getUnlockedTechnologies() {
        return Collections.unmodifiableSet(unlockedTechnologies);
    }

    /**
     * Returns the id of the technology currently being researched, or
     * {@code null} if the player has no active research.
     *
     * @return active research id, or {@code null}
     */
    public ResourceLocation getActiveResearch() {
        return activeResearch;
    }

    /**
     * Sets the currently active research.  Pass {@code null} to clear it and
     * reset progress.
     *
     * @param technologyId technology id, or {@code null}
     */
    public void setActiveResearch(ResourceLocation technologyId) {
        this.activeResearch = technologyId;
        this.researchProgress = 0;
    }

    /**
     * Returns the current research-progress counter (in science-pack ticks).
     *
     * @return progress ticks
     */
    public int getResearchProgress() {
        return researchProgress;
    }

    /**
     * Increments the progress counter.
     *
     * @param amount ticks to add
     */
    public void addProgress(int amount) {
        researchProgress += amount;
    }

    /**
     * Resets the progress counter to zero (called when a technology is
     * completed or abandoned).
     */
    public void resetProgress() {
        researchProgress = 0;
    }

    // -----------------------------------------------------------------
    // NBT serialisation
    // -----------------------------------------------------------------

    /**
     * Serialise this data to an NBT {@link CompoundTag}.
     *
     * @return serialised tag
     */
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        ListTag unlockedList = new ListTag();
        for (ResourceLocation id : unlockedTechnologies) {
            unlockedList.add(StringTag.valueOf(id.toString()));
        }
        tag.put(TAG_UNLOCKED, unlockedList);

        if (activeResearch != null) {
            tag.putString(TAG_ACTIVE, activeResearch.toString());
        }
        tag.putInt(TAG_PROGRESS, researchProgress);

        return tag;
    }

    /**
     * Restore this data from an NBT {@link CompoundTag}.
     *
     * @param tag serialised tag
     */
    public void load(CompoundTag tag) {
        unlockedTechnologies.clear();
        ListTag unlockedList = tag.getList(TAG_UNLOCKED, Tag.TAG_STRING);
        for (int i = 0; i < unlockedList.size(); i++) {
            unlockedTechnologies.add(ResourceLocation.parse(unlockedList.getString(i)));
        }

        if (tag.contains(TAG_ACTIVE)) {
            activeResearch = ResourceLocation.parse(tag.getString(TAG_ACTIVE));
        } else {
            activeResearch = null;
        }
        researchProgress = tag.getInt(TAG_PROGRESS);
    }
}
