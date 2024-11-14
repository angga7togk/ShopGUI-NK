# ShopGUI-PNX
ShopGUI plugin for Nukkit and Nukkit MOT

## Getting Started

### Installation
1. Download the latest release from [ShopGUI](https://github.com/angga7togk/ShopGUI-PNX/releases).
2. Download the depend plugin [Dependency](#dependency).
3. Place the JAR file into the `plugins` folder of your Nukkit server.
4. Start or reload your server.

### Dependency
1. [FormAPI](https://github.com/ScarletRedMan/FormAPI).
2. [FakeInventories v1.1.8](https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.8) or [FakeInventories-Nukkit-MOT](https://github.com/angga7togk/FakeInventories-Nukkit-MOT/releases/tag/mot)
3. [Economy-Angga7Togk](https://github.com/angga7togk/EconomyAPI-NK/releases) or [Economy-OneBone](https://github.com/Nukkit-coders/EconomyAPI)

### Economy Depend Supported
1. EconomyAPI (Angga7Togk) [EconomyAPI](https://github.com/angga7togk/EconomyAPI-PNX)
2. EconomyAPI (Onebone) [EconomyAPI](https://cloudburstmc.org/resources/economyapi.14)

## Commands For Operator
| Command                            | Description            | use |
|------------------------------------|------------------------|-----|
| `/shop`                            | open shop menu         | all |
| `/shop <category>`                 | open shop via category | all |
| `/shop addcategory <category>`     | add category shop      | op  |
| `/shop removecategory <category>`  | remove category shop   | op  |
| `/shop additem <category> <price>` | sell item on shop      | op  |
| `/shop removeitem <category>`      | un sell item on shop   | op  |