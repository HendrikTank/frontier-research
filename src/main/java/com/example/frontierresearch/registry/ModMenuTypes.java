package com.example.frontierresearch.registry;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.menu.BurnerLabMenu;
import com.example.frontierresearch.menu.ResearchTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all {@link MenuType}s for Frontier Research.
 */
public final class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, FrontierResearch.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ResearchTableMenu>> RESEARCH_TABLE =
            MENU_TYPES.register("research_table",
                    () -> IMenuTypeExtension.create(
                            (windowId, inv, buf) -> new ResearchTableMenu(windowId, inv, buf.readBlockPos())
                    )
            );

    public static final DeferredHolder<MenuType<?>, MenuType<BurnerLabMenu>> BURNER_LAB =
            MENU_TYPES.register("burner_lab",
                    () -> IMenuTypeExtension.create(
                            (windowId, inv, buf) -> new BurnerLabMenu(windowId, inv, buf.readBlockPos())
                    )
            );

    private ModMenuTypes() {}

    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}
