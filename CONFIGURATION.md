# Frontier Research - Configuration Guide

This guide is for modpack makers, server administrators, and players who want to customize the research system.

## Future Configuration Options

When fully implemented, Frontier Research will support extensive configuration through config files and data packs.

### Research Configuration (Data Driven)

Research entries will be fully data-driven through JSON files in data packs. This allows easy customization without code changes.

#### Example Research Entry

Create a file at `data/yourpack/research/custom_research.json`:

```json
{
  "id": "yourpack:custom_research",
  "tier": 2,
  "required_progress": 300,
  "prerequisites": [
    "frontierresearch:basic_automation",
    "frontierresearch:advanced_machinery"
  ],
  "science_packs": [
    {
      "item": "frontierresearch:basic_science_pack",
      "count": 20
    },
    {
      "item": "frontierresearch:advanced_science_pack",
      "count": 10
    }
  ],
  "unlocks": [
    "minecraft:dispenser",
    "minecraft:dropper",
    "yourmod:custom_machine"
  ]
}
```

### Science Pack Configuration

Science packs can be configured via data packs to change their tier and research value:

```json
{
  "item": "frontierresearch:basic_science_pack",
  "tier": 0,
  "research_value": 10,
  "valid_for": ["*"]
}
```

### Lab Configuration

Lab properties will be configurable through common configs:

```toml
[labs]
    [labs.research_table]
        tier = 1
        speed_multiplier = 1.0
        max_science_slots = 9
    
    [labs.burner_lab]
        tier = 2
        speed_multiplier = 1.5
        max_science_slots = 9
        fuel_efficiency = 1.0
    
    [labs.multi_block_electric]
        tier = 4
        speed_multiplier = 3.0
        max_science_slots = 18
        power_per_tick = 100
        max_upgrades = 2
```

### Recipe Locking Configuration

Control which recipes are locked behind research:

```toml
[recipe_locking]
    # Enable/disable recipe locking entirely
    enabled = true
    
    # Lock vanilla recipes behind research
    lock_vanilla_recipes = true
    
    # Specific recipes to lock
    locked_recipes = [
        "minecraft:hopper",
        "minecraft:dropper",
        "minecraft:piston"
    ]
```

### Progression Configuration

Adjust research progression rates:

```toml
[progression]
    # Global research speed multiplier
    global_speed = 1.0
    
    # Science pack value multipliers by tier
    tier_0_multiplier = 1.0
    tier_1_multiplier = 1.0
    tier_2_multiplier = 1.0
    
    # Manual research (picking up items)
    manual_research_enabled = true
    manual_research_progress_per_item = 5
```

## Integration with Other Mods

### JEI/REI Integration

Research requirements will be displayed in recipe viewers:
- Locked recipes show a red lock icon
- Clicking the lock shows required research
- Research dependencies are displayed as a tree

### Create Integration

Example integration with Create mod:
- Create machines can be locked behind mechanical research
- Kinetic energy can power electric labs
- Create's assembly system can craft science packs

### Applied Energistics 2 Integration

- ME networks can automate science pack production
- Crafting processors can be locked behind digital research
- Quantum research requires cryo-lab

### Thermal Expansion Integration

- Machines can be locked behind thermodynamics research
- RF can power electric labs
- Magmatic research unlocked by lava interaction

## Modpack Examples

### Tech-Only Modpack

Focus on research-gated technology progression:

```toml
[progression]
    manual_research_enabled = false
    global_speed = 0.5  # Slower progression
    
[recipe_locking]
    lock_vanilla_recipes = true
```

### Kitchen Sink Modpack

Balanced progression with multiple mod integrations:

```toml
[progression]
    manual_research_enabled = true
    global_speed = 1.0
    
[recipe_locking]
    lock_vanilla_recipes = false  # Only lock modded content
```

### Expert Mode

Very slow progression with extensive locking:

```toml
[progression]
    manual_research_enabled = false
    global_speed = 0.25
    
[recipe_locking]
    enabled = true
    lock_vanilla_recipes = true
    
[labs.burner_lab]
    fuel_efficiency = 0.5  # Uses fuel faster
```

## Server Administration

### Commands (Planned)

```
/research list [player] - List completed research for a player
/research grant <player> <research_id> - Grant research to a player
/research reset <player> [research_id] - Reset research progress
/research info <research_id> - Show research information
```

### Permissions

Compatible with permission mods like LuckPerms:
- `frontierresearch.command.list` - View research status
- `frontierresearch.command.grant` - Grant research
- `frontierresearch.command.reset` - Reset research
- `frontierresearch.command.admin` - All admin commands

## Troubleshooting

### Common Issues

**Issue**: Research isn't progressing
- Check if lab has science packs
- Check if lab has fuel (burner labs)
- Verify research prerequisites are met

**Issue**: Recipes are locked even with research completed
- Reload the world/server
- Check if recipe locking is enabled
- Verify research data isn't corrupted

**Issue**: Multi-block structure won't form
- Check structure pattern against documentation
- Ensure all blocks are placed correctly
- Check for obstructions in required air spaces

## Contributing Research Entries

To contribute research entries to the default set:

1. Create the research JSON file
2. Test thoroughly in various scenarios
3. Submit a pull request to the GitHub repository
4. Include documentation of what the research unlocks

## Support

For support, please:
1. Check the GitHub issues page
2. Join the Discord server (if available)
3. Read the API documentation (API.md)
