package com.example.frontierresearch.api;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Central API for the Frontier Research mod.
 *
 * <p>Other mods (and the mod itself) use this class to register and query
 * science packs, technologies, and lab types at mod-initialisation time.</p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // In your mod's constructor or FMLCommonSetupEvent:
 * FrontierResearchAPI.registerSciencePack(new SciencePack(
 *         ResourceLocation.fromNamespaceAndPath("mymod", "magic_science_pack"),
 *         () -> MyModItems.MAGIC_SCIENCE_PACK.get(),
 *         0, 180, 255
 * ));
 *
 * FrontierResearchAPI.registerTechnology(
 *         Technology.builder(ResourceLocation.fromNamespaceAndPath("mymod", "magic_casting"))
 *                 .tier(LabTier.ELECTRIC_LAB)
 *                 .costs(ResourceLocation.fromNamespaceAndPath("mymod", "magic_science_pack"), 10)
 *                 .unlocks(ResourceLocation.fromNamespaceAndPath("mymod", "magic_staff"))
 *                 .build()
 * );
 * }</pre>
 */
public final class FrontierResearchAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontierResearchAPI.class);

    private static final Map<ResourceLocation, SciencePack> SCIENCE_PACKS = new LinkedHashMap<>();
    private static final Map<ResourceLocation, Technology> TECHNOLOGIES = new LinkedHashMap<>();
    private static final Map<ResourceLocation, ILabType> LAB_TYPES = new LinkedHashMap<>();

    private FrontierResearchAPI() {}

    // ------------------------------------------------------------------
    // Registration
    // ------------------------------------------------------------------

    /**
     * Register a new science pack.
     *
     * <p>Must be called before {@code FMLCommonSetupEvent} is complete.
     * Duplicate ids are rejected with a warning and the existing entry is kept.</p>
     *
     * @param pack the science pack to register (non-null)
     */
    public static void registerSciencePack(SciencePack pack) {
        Objects.requireNonNull(pack, "SciencePack must not be null");
        if (SCIENCE_PACKS.putIfAbsent(pack.id(), pack) != null) {
            LOGGER.warn("[FrontierResearch] Duplicate SciencePack id '{}' – ignoring registration.", pack.id());
        } else {
            LOGGER.debug("[FrontierResearch] Registered SciencePack: {}", pack.id());
        }
    }

    /**
     * Register a new technology.
     *
     * <p>Duplicate ids are rejected with a warning.</p>
     *
     * @param technology the technology to register (non-null)
     */
    public static void registerTechnology(Technology technology) {
        Objects.requireNonNull(technology, "Technology must not be null");
        if (TECHNOLOGIES.putIfAbsent(technology.getId(), technology) != null) {
            LOGGER.warn("[FrontierResearch] Duplicate Technology id '{}' – ignoring registration.", technology.getId());
        } else {
            LOGGER.debug("[FrontierResearch] Registered Technology: {}", technology.getId());
        }
    }

    /**
     * Register a new lab type.
     *
     * <p>Duplicate ids are rejected with a warning.</p>
     *
     * @param labType the lab type to register (non-null)
     */
    public static void registerLabType(ILabType labType) {
        Objects.requireNonNull(labType, "ILabType must not be null");
        if (LAB_TYPES.putIfAbsent(labType.getId(), labType) != null) {
            LOGGER.warn("[FrontierResearch] Duplicate ILabType id '{}' – ignoring registration.", labType.getId());
        } else {
            LOGGER.debug("[FrontierResearch] Registered ILabType: {}", labType.getId());
        }
    }

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

    /**
     * Look up a registered science pack by id.
     *
     * @param id registry id
     * @return an {@link Optional} wrapping the pack, or empty if not found
     */
    public static Optional<SciencePack> getSciencePack(ResourceLocation id) {
        return Optional.ofNullable(SCIENCE_PACKS.get(id));
    }

    /**
     * Look up a registered technology by id.
     *
     * @param id registry id
     * @return an {@link Optional} wrapping the technology, or empty if not found
     */
    public static Optional<Technology> getTechnology(ResourceLocation id) {
        return Optional.ofNullable(TECHNOLOGIES.get(id));
    }

    /**
     * Look up a registered lab type by id.
     *
     * @param id registry id
     * @return an {@link Optional} wrapping the lab type, or empty if not found
     */
    public static Optional<ILabType> getLabType(ResourceLocation id) {
        return Optional.ofNullable(LAB_TYPES.get(id));
    }

    /**
     * Returns an unmodifiable view of all registered science packs.
     *
     * @return unmodifiable collection of science packs
     */
    public static Collection<SciencePack> getAllSciencePacks() {
        return Collections.unmodifiableCollection(SCIENCE_PACKS.values());
    }

    /**
     * Returns an unmodifiable view of all registered technologies.
     *
     * @return unmodifiable collection of technologies
     */
    public static Collection<Technology> getAllTechnologies() {
        return Collections.unmodifiableCollection(TECHNOLOGIES.values());
    }

    /**
     * Returns an unmodifiable view of all registered lab types.
     *
     * @return unmodifiable collection of lab types
     */
    public static Collection<ILabType> getAllLabTypes() {
        return Collections.unmodifiableCollection(LAB_TYPES.values());
    }

    /**
     * Returns all technologies that are compatible with the given lab tier
     * (i.e. technologies whose required tier is &le; the provided tier).
     *
     * @param tier the lab tier to filter by
     * @return list of compatible technologies (may be empty)
     */
    public static List<Technology> getTechnologiesForTier(LabTier tier) {
        List<Technology> result = new ArrayList<>();
        for (Technology tech : TECHNOLOGIES.values()) {
            if (tier.canResearch(tech.getRequiredTier())) {
                result.add(tech);
            }
        }
        return result;
    }
}
