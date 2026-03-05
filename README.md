# Frontier Research

A NeoForge mod for Minecraft 1.21.1 that adds a Factorio-style research progression system.

## Features

- **Research System**: Unlock recipes and technologies by conducting research
- **Multiple Lab Tiers**: Progress from manual research to advanced multi-block structures
- **Science Packs**: Consumable items used to progress research
- **Extensible API**: Easy integration for mod developers
- **Optional Integrations**: Support for CC:Tweaked, MoreRed, and other mods

## Lab Tiers

1. **Manual Research**: Pick up and inspect items to unlock basic recipes
2. **Research Table**: Single-block structure for basic research
3. **Burner Lab**: Fuel-powered lab with increased research speed
4. **Multi-Block Burner Lab**: Advanced burner-based research (planned)
5. **Multi-Block Electric Lab**: Electric-powered lab with upgrade slots (planned)
6. **Multi-Block Cryo-Lab**: Cryogenic cooling for advanced research (planned)

## Science Packs

- **Basic Science Pack**: For tier 0-1 research
- **Advanced Science Pack**: For tier 1-2 research
- More science packs can be added via the API

## API

See [API.md](API.md) for details on:
- Registering custom science packs
- Creating custom research entries
- Implementing custom lab types
- Integration with other mods

## Installation

1. Download the latest release from the releases page
2. Place the JAR file in your `mods` folder
3. Launch Minecraft with NeoForge

## Development

### Building

```bash
./gradlew build
```

### Running

```bash
./gradlew runClient  # Run the game client
./gradlew runServer  # Run the dedicated server
```

## License

MIT License - See TEMPLATE_LICENSE.txt for details

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

