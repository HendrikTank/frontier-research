package com.hendrictank.frontierresearch.integration;

/**
 * Optional integration for CC:Tweaked (ComputerCraft).
 * This class provides computer peripheral support for research labs.
 * 
 * To use this integration:
 * 1. Add CC:Tweaked as an optional dependency in your build.gradle
 * 2. Implement IPeripheral interface from CC:Tweaked
 * 3. Register peripheral providers for lab blocks
 * 
 * Example usage in Lua:
 * 
 * local lab = peripheral.wrap("front")
 * lab.setResearch("frontierresearch:basic_automation")
 * local progress = lab.getProgress()
 * local research = lab.getCurrentResearch()
 */
public class CCTweakedIntegration {
    
    private static boolean isLoaded = false;
    
    /**
     * Initialize CC:Tweaked integration if the mod is present
     */
    public static void init() {
        try {
            Class.forName("dan200.computercraft.api.peripheral.IPeripheral");
            isLoaded = true;
            // TODO: Register peripheral providers when CC:Tweaked is present
        } catch (ClassNotFoundException e) {
            // CC:Tweaked not present, skip integration
            isLoaded = false;
        }
    }
    
    public static boolean isLoaded() {
        return isLoaded;
    }
}
