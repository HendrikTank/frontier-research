# Frontier Research API

This document describes how to use the Frontier Research API to add custom science packs, research entries, and lab types.

## Registering Custom Science Packs

Science packs are items that implement the `ISciencePack` interface.

```java
public class CustomSciencePack extends Item implements ISciencePack {
    @Override
    public int getTier() {
        return 2; // Tier level
    }
    
    @Override
    public int getResearchValue() {
        return 50; // Research points per pack
    }
}
```

Register your science pack like any other item using NeoForge's deferred register system.

## Registering Custom Research

Use the `ResearchRegistry` to register new research entries:

```java
ResearchRegistry.register(new Research.Builder(
        ResourceLocation.fromNamespaceAndPath("yourmod", "custom_research"))
        .tier(1)
        .requiredProgress(200)
        .prerequisites(List.of(
            ResourceLocation.fromNamespaceAndPath("frontierresearch", "basic_automation")
        ))
        .sciencePacks(List.of(
            new ItemStack(ModItems.BASIC_SCIENCE_PACK.get(), 20)
        ))
        .unlocks(() -> List.of(
            ResourceLocation.fromNamespaceAndPath("yourmod", "custom_item")
        ))
        .build());
```

## Creating Custom Lab Types

Custom labs should implement the `IResearchLab` interface:

```java
public class CustomLabBlockEntity extends BlockEntity implements IResearchLab {
    @Override
    public int getLabTier() {
        return 3; // Lab tier
    }
    
    @Override
    public double getResearchSpeed() {
        return 2.0; // 2x speed multiplier
    }
    
    @Override
    public boolean canResearch(String researchId) {
        // Implement logic to determine if this lab can conduct the research
        return true;
    }
    
    @Override
    public boolean progressResearch(String researchId) {
        // Implement logic to consume science packs and progress research
        return true;
    }
    
    @Override
    public String getCurrentResearch() {
        return currentResearch;
    }
    
    @Override
    public void setCurrentResearch(String researchId) {
        this.currentResearch = researchId;
    }
}
```

## Integration APIs

### CC:Tweaked Integration

The mod provides optional CC:Tweaked integration for computer-controlled research labs.

Example Lua script:
```lua
local lab = peripheral.wrap("right")
lab.setResearch("frontierresearch:basic_automation")
local progress = lab.getProgress()
print("Research progress: " .. progress .. "%")
```

### Redstone Integration

Labs support redstone signals:
- Comparator output: Reflects research progress (0-15 signal strength)
- Redstone pulse: Can be used to start/stop research

## Player Research Data

Access player research data using the attachment system:

```java
PlayerResearchData data = player.getData(ModAttachments.PLAYER_RESEARCH);
if (data.hasCompleted(researchId)) {
    // Player has completed this research
}
data.addProgress(researchId, 10);
```

## Lab Tiers

Built-in lab tiers:
- Tier 0: Manual Research (picking up/inspecting items)
- Tier 1: Research Table
- Tier 2: Burner Lab
- Tier 3: Multi-Block Burner Lab (planned)
- Tier 4: Multi-Block Electric Lab (planned)
- Tier 5: Multi-Block Cryo-Lab (planned)

Higher tier labs can research faster and handle more advanced research entries.
