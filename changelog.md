## Ex Deorum 1.10
- Added crucibles, sieves, and barrels for wood types from Blue Skies and Aether mods.
- Added Certus Quartz Dust as a sieve drop from Dust.
- Allow extracting water from wooden crucibles using water bottles.
- Watering cans now accelerate crops from Pam's Harvestcraft 2 Crops
- Fixed a crash that happened with Ex Deorum barrels and crucibles when logging out of a world.

## Ex Deorum 1.9
- Fixed incompatibility with SkyblockBuilder where player would not receive torch/watering can and Ex Deorum advancements
- Fixed error message printing while patching the End Portal method
- Fixed several issues with hammers in LAN and server worlds
- Fixed hammer recipes with tag ingredients not functioning

## Ex Deorum 1.8
- Added a config option to limit the number of sieve drops from sieving moss. May be useful when playing with mods that add a lot of different saplings.
- Added compatibility with Ars Nouveau's saplings and Sourceberries to the sieve.
- Added compatibility with Ars Nouveau's wood types for Crucibles, Barrels, and Sieve.
- Added missing TOP compatibility for seeing fluids inside Barrels and Crucibles. The exact fluid and amount are now visible when crouching.
- Added Witch Water entity conversion functionality from the original Ex Nihilo, can be disabled in config. Here are the following conversions: Villager -> Zombie Villager, Cleric Villager -> Witch, Skeleton -> Wither Skeleton, Creeper -> Charged Creeper, Spider -> Cave Spider, Pig & Piglin -> Zombified Piglin, Squid -> Ghast, Mooshroom -> Brown Mooshroom, Axolotl -> Blue Axolotl, Rabbit -> Killer Rabbit, Pufferfish -> Guardian, Horse -> Skeleton/Zombie Horse
- Allow using glass bottles to carry water from wooden crucibles to barrels. Can be disabled in config.
- Fix Mekanism's Tin ore not being detected by Ex Deorum as a valid tin ore.

## Ex Deorum 1.7
- Actually added way to obtain Sky Stone Dust from AE2.
- Added compatibility with Factorium's ores, which can now be crafted with Ex Deorum's ore chunks.
- Hammer now has slight benefits for using Fortune. For blocks with multiple drops, slightly increases the number of drops, and for blocks which have a chance to not drop anything, decreases the chance that nothing is dropped.

## Ex Deorum 1.6
- Added way to obtain sky stone dust from AE2.
- Fixed End Portal not generating properly.
- Fixed missing Biomes O' Plenty recipes for the sieves, barrels, and crucibles.

## Ex Deorum 1.5
- Added Random Pottery Sherd and Random Armor Trim items to replace the bloated sand loot tables for obtaining pottery sherds and armor trims.
- Added compatibility with Biomes O' Plenty (sieves, barrels, crucibles, saplings obtainable from sifting moss like other saplings) 
- Added compatibility with Extreme Reactors
- Added WIP compatibility with Applied Energistics 2
- Added compatibility with KubeJS for Crucibles, with methods called `exdeorum.setCrucibleHeatValue(BlockState, int)` and `exdeorum.setCrucibleHeatValueForBlock(Block, int)`
- Fixed an incorrect error message saying "Unable to grant player the Void World advancement" when the player already has it
- Fixed a bug with the crucible filling up too quickly.
- Added a config option to change which configured_feature to use when generating the spawn island
- Added a config option to use a different spawn tree based on the biome the player spawns in

## Ex Deorum 1.4
- Red Sand is now obtainable by hammering crushed netherrack.
- Sieves can no longer be automated by machines (can be re-enabled in the server config.)
- Fixed desync bug when a machine like Create's Deployer interacts with the Sieve
- Fixed an edge case where a modded ore wouldn't appear if the item form wasn't in the ore's tag.
- Fixed #5, which prevented automating the Crucible

## Ex Deorum 1.3
- Allow modded ores to drop from sieving Gravel and Crushed Deepslate if a mod with that ore is installed. Supported ores are Aluminum, Cobalt, Silver, Lead, Platinum, Nickel, Uranium, Osmium, Tin, Zinc, and Iridium.
- Allow some modded drops from sieving if a mod with those resources is installed. Currently, only Grains of Infinity from Ender IO is supported.
- If you would like to see some more mod compatibility related additions to Ex Deorum, open an issue on GitHub or let me know on Discord.

## Ex Deorum 1.2
- Fixed the missing recipes for Deepslate pebbles

## Ex Deorum 1.1
- Fixed the missing barrel mixing recipe for Clay Block

## Ex Deorum 1.0  
*Ex Deorum*'s new additions are listed first, then the additions from original *Ex Nihilo*.
- Added the Watering Can, which can be used to hydrate farmland and speed up plant and tree growth. It is intended to be
an alternative to mods like Twerk or Tree Growing Simulator, as a Wooden Watering Can is given to the player at the start (configurable).
The list of blocks that Watering Cans can be used on is defined by the tag `exdeorum:watering_can_blocks`.
- Added the Sculk Core, which can be used to enable player-placed Sculk Shriekers to spawn Wardens.
- Added Witch Water, which can be used to create a variety of blocks with the barrel AND it can be used to create a Netherrack generator,
replacing where regular Water would be used. (configurable)
- Added the Sieve, which uses sieve meshes to filter items. (drops are configurable through data pack)
- Added the Porcelain, Crimson, and Warped Crucibles which melt cobblestone into lava when placed above a heat source. (configurable through data pack, supports modded fluids)
- Added Wooden Crucibles which melt plant material into water. (configurable through data pack, supports modded fluids)
- Added the Barrel, which has all of its functionality from Ex Nihilo. (Composting and Mixing recipes are configurable through data pack)
- Added the Silk Worm, which can infest a tree so that can be harvested for string. It can also be cooked and eaten... if you're desperate.
- Added the Crook, which can be used to break leaves and infested leaves for increased drop rates.
- Added the Hammer, which can be used to crush and smash blocks. (recipes are configurable through data pack)
- Added the porcelain bucket, a cheap alternative to a regular bucket in the early game with the catch that it breaks after pouring out lava.