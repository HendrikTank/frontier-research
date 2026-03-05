package com.example.frontierresearch.api;

/**
 * Defines the tier hierarchy for research labs in Frontier Research.
 *
 * <p>Higher-tier labs can process more complex science packs and research
 * technologies faster.  Each tier is strictly ordered: a lab of a given tier
 * can research any {@link Technology} that requires a tier equal to or lower
 * than itself.</p>
 *
 * <p>Tier progression:</p>
 * <ol>
 *   <li>{@link #MANUAL} – the player themselves (item pickup / hand-use)</li>
 *   <li>{@link #RESEARCH_TABLE} – single-block research table, no fuel</li>
 *   <li>{@link #BURNER_LAB} – single-block burner lab</li>
 *   <li>{@link #BURNER_LAB_MULTI} – multi-block burner lab</li>
 *   <li>{@link #ELECTRIC_LAB} – multi-block electric lab (base)</li>
 *   <li>{@link #ELECTRIC_LAB_T2} – upgraded electric lab (tier 2)</li>
 *   <li>{@link #ELECTRIC_LAB_T3} – fully upgraded electric lab (tier 3)</li>
 *   <li>{@link #CRYO_LAB} – electric lab that also requires a liquid coolant</li>
 * </ol>
 */
public enum LabTier {

    /** Manual research performed by the player (item pick-up or hand-use). */
    MANUAL(0),

    /** Single-block research table; no fuel required. */
    RESEARCH_TABLE(1),

    /** Single-block burner lab; consumes solid fuel. */
    BURNER_LAB(2),

    /** Multi-block burner lab; consumes solid fuel at a higher rate. */
    BURNER_LAB_MULTI(3),

    /** Multi-block electric lab (base tier); consumes Forge Energy. */
    ELECTRIC_LAB(4),

    /** Multi-block electric lab (tier 2 upgrade). */
    ELECTRIC_LAB_T2(5),

    /** Multi-block electric lab (tier 3 – fully upgraded). */
    ELECTRIC_LAB_T3(6),

    /** Multi-block cryo-cooled electric lab; requires a liquid coolant in addition to FE. */
    CRYO_LAB(7);

    private final int level;

    LabTier(int level) {
        this.level = level;
    }

    /**
     * Returns the numeric level of this tier (higher = more advanced).
     *
     * @return numeric tier level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns {@code true} if this tier is capable of researching a technology
     * that requires the given {@code required} tier.
     *
     * @param required the minimum tier demanded by a technology
     * @return {@code true} when this tier &ge; required tier
     */
    public boolean canResearch(LabTier required) {
        return this.level >= required.level;
    }
}
