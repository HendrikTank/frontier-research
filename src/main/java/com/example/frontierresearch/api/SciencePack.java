package com.example.frontierresearch.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Represents a science-pack type that can be consumed by research labs.
 *
 * <p>Science packs are the "fuel" of the research system.  Each technology
 * lists which packs (and how many of each) it requires.  Mods can register
 * their own science packs via {@link FrontierResearchAPI#registerSciencePack}.</p>
 *
 * @param id    the unique registry id, e.g. {@code frontierresearch:basic_science_pack}
 * @param item  a lazy supplier returning the {@link Item} that represents this pack in-game
 * @param color an ARGB tint used when rendering the pack icon (0 = no tint)
 */
public record SciencePack(
        ResourceLocation id,
        Supplier<Item> item,
        int color
) {

    /**
     * Convenience constructor that derives the color from the RGB components
     * (alpha is forced to 255).
     *
     * @param id    registry id
     * @param item  item supplier
     * @param r     red 0–255
     * @param g     green 0–255
     * @param b     blue 0–255
     */
    public SciencePack(ResourceLocation id, Supplier<Item> item, int r, int g, int b) {
        this(id, item, (0xFF << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
    }

    @Override
    public String toString() {
        return "SciencePack[" + id + "]";
    }
}
