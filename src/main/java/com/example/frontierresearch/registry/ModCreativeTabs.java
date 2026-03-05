package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers the Frontier Research creative mode tab.
 */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrontierResearch.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FRONTIER_RESEARCH_TAB =
            CREATIVE_MODE_TABS.register("frontier_research_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.frontierresearch"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> ModItems.BASIC_SCIENCE_PACK.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                // Science packs
                                output.accept(ModItems.BASIC_SCIENCE_PACK.get());
                                output.accept(ModItems.AUTOMATION_SCIENCE_PACK.get());
                                output.accept(ModItems.APPLIED_SCIENCE_PACK.get());
                                output.accept(ModItems.ADVANCED_SCIENCE_PACK.get());
                                output.accept(ModItems.CRYO_SCIENCE_PACK.get());
                                // Lab blocks
                                output.accept(ModItems.RESEARCH_TABLE_ITEM.get());
                                output.accept(ModItems.BURNER_LAB_ITEM.get());
                            })
                            .build()
            );

    private ModCreativeTabs() {}

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
