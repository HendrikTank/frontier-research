package com.example.frontierresearch.api;

import net.minecraft.resources.ResourceLocation;

/**
 * Interface that every research-lab type must implement.
 *
 * <p>Lab types represent a category of research machine (e.g. Burner Lab,
 * Electric Lab).  Each instance describes <em>which</em> science packs the
 * lab can accept and how fast it operates relative to the baseline.</p>
 *
 * <p>Register custom lab types via {@link FrontierResearchAPI#registerLabType}.</p>
 */
public interface ILabType {

    /**
     * Returns the unique registry id of this lab type, e.g.
     * {@code frontierresearch:burner_lab}.
     *
     * @return registry id (non-null)
     */
    ResourceLocation getId();

    /**
     * Returns the {@link LabTier} this lab type belongs to.
     *
     * @return lab tier
     */
    LabTier getTier();

    /**
     * Returns {@code true} if this lab type can accept and process the
     * specified science pack.
     *
     * <p>By default all lab types accept any pack registered via
     * {@link FrontierResearchAPI}.  Override to restrict to a subset.</p>
     *
     * @param pack the science pack to test
     * @return {@code true} if accepted
     */
    default boolean canProcess(SciencePack pack) {
        return true;
    }

    /**
     * Returns a multiplier applied to the base research speed of this lab.
     * A value of {@code 1.0} means normal speed; {@code 2.0} means twice
     * as fast, etc.
     *
     * @return speed multiplier (&gt; 0)
     */
    default float getSpeedMultiplier() {
        return 1.0f;
    }

    /**
     * Returns {@code true} if this lab type requires solid fuel (like a
     * furnace).  Labs that return {@code true} will look for fuel in a
     * dedicated slot.
     *
     * @return {@code true} for fuel-consuming labs
     */
    default boolean requiresFuel() {
        return false;
    }

    /**
     * Returns {@code true} if this lab type requires Forge Energy (FE) input.
     *
     * @return {@code true} for electrically-powered labs
     */
    default boolean requiresEnergy() {
        return false;
    }

    /**
     * Returns {@code true} if this lab type requires a liquid coolant (e.g.
     * the Cryo-Lab).
     *
     * @return {@code true} for coolant-requiring labs
     */
    default boolean requiresCoolant() {
        return false;
    }
}
