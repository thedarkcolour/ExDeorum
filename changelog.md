## Ex Deorum 1.45
- Allow changing drops for Random Armor Trim and Pottery Sherd, also add Tide to possible trims (#138)
- Add compatibility with Modern Industrialization, Forestry, Nature Arise ()

## Ex Deorum 1.44
- Fix Silkworms not applying to certain modded leaves like TFC

## Ex Deorum 1.43
- Fix sieve recipe bug with latest version of JEI

## Ex Deorum 1.42
- Fix JEI sieve recipe bug

## Ex Deorum 1.41
- Add native EMI support.
- Fix bug where removing all Compressed Sieve recipes would break regular Sieve recipe display in JEI.
- Hack fix for random crashes with fluid transformation recipe cache

## Ex Deorum 1.40
- Buffed melt rate of water crucible. Should now be comparable to what it was in older versions.
- It is now possible to add custom Compressed Sieve block types. Their JSON schema is exactly the same as it is for regular sieve blocks.

## Ex Deorum 1.39
- Fixed third-person render of compressed hammers
- Fixed crash with Croptopia
- Witch water buckets now dispense properly
- Wooden crucibles/barrels are now furnace fuels

## Ex Deorum 1.38
- Fix rendering bug with Crucible and Barrel
- Fix lag spike when inserting compost into the Barrel for the first time
- Fix June barrel config

## Ex Deorum 1.37
- Fix issue with Inventory Tweaks item restocking for sieves
- Fix sieving not using more than 64 sieves at a time when high sieve ranges were enabled
- Fix desync bug with barrels that sometimes happens when emptying/filling with a bucket
- Fix overriding Superflat world type

## Ex Deorum 1.36
- Add config options to prevent barrels and crucibles from collecting rainwater
- Fluids in barrels and crucibles now affect mobs standing inside them
- Add `exdeorum:ore_chunks` tag for use in Tag Filters from item transport mods
- Add distinct sound events for Ex Deorum sounds so they can be easily detected by Sound Muffler mods. Here are the sound IDs:
  - `exdeorum:barrel_add_compost`: Played when compost is added to the barrel
  - `exdeorum:barrel_compost`: Played when the barrel finished composting dirt
  - `exdeorum:barrel_mixing`: Played when a block is mixed in the barrel (ex. clay)
  - `exdeorum:barrel_fluid_transform`: Played when a fluid finishes transforming in a barrel (ex. witch water)
  - `exdeorum:silk_worm_drop`: Played when a silk worm is dropped from leaves harvested by a crook
  - `exdeorum:silk_worm_infest`: Played when a silk worm is used on a leaves block
  - `exdeorum:silk_worm_eat`: Played when a player eats a cooked silk worm
  - `exdeorum:grass_seeds_place`: Played when grass seeds or nylium spores are used on grass or netherrack
  - `exdeorum:sculk_core_activate`: Played when a sculk core is used on a sculk shrieker to spawn Wardens
  - `exdeorum:watering_can_use`: Played when a watering can is watering something
  - `exdeorum:watering_can_stop`: Played when a player stops using the watering can
- Fixed fluid mixing sounds playing at the same volume regardless of distance from the barrel
- Add note to Random Armor Trim indicating it does not drop Netherite Upgrade template
- Changed default value of server config `simultaneous_compressed_sieve_usage` to true so compressed sieves aren't terrible anymore

## Ex Deorum 1.35
- Fixed bug where barrels would not trigger a transformation recipe while it is raining (ex. Witch Water conversion)
- Fixed barrel fluid transformation recipes ignoring the result fluid and only crafting witch water 
- Added clientside config option to disable rainbow compost in barrels during June

## Ex Deorum 1.34
- Fix overriding default world type in Create World screen when common config option set_void_world_as_default is false.
- Removed set_void_world_as_default option from client config, you must now only use the option from the common config.

## Ex Deorum 1.33
- Fix typo in JEI integration where both sieve categories were "Compressed Sieve".
- Add integration for SkyBlock Builder. Now, if that mod is installed, SkyBlock builder's preset is chosen by default instead of Ex Deorum's. This goes for the server.properties file too.
- Added config option for using compressed sieves simultaneously.
- Added Immersive Engineering integration for Ex Deorum's ore chunks.

## Ex Deorum 1.32
- Fixed an issue with void worlds not generating bastion remnants, nether fortresses, or obsidian pillars in the End

## Ex Deorum 1.31
- Fixed a server error that would kick players who sprinted on top of Infested Leaves
- All recipes now have NBT support for their results (ex. it is now possible to add enchanted books as a sieve drop)

## Ex Deorum 1.30
- EMI compatibility
- Compressed hammers, sieves, and blocks
- Compatibility with AllTheCompressed and Compressium (however, AllTheCompressed is better because it has more blocks than Compressium.)

## Ex Deorum 1.29
- Add a config option to disable the automated_sieve nerf (now you can enable machines to use multiple sieves simultaneously)

## Ex Deorum 1.28
- Fix a bug from 1.26 that made Witch Water crafted from Lava instead of Water

## Ex Deorum 1.27
- Fix a dupe glitch with Mechanical Hammer

## Ex Deorum 1.26
- Fluid transformation recipes are now data driven
- Increased drop rate of Iron Ore Chunk
- Updated documentation to explain how to add Ex Deorum recipes through JSON data packs. If you are using KubeJS/CraftTweaker, this will probably be useful to you as well. Check it out [here](https://exdeorum.readthedocs.io/en/latest/datapack/).
- Fixed Witch Water spawning mushrooms when not converting (now, they only spawn while converting from water to witch water)
- Improved color transition used in witch water conversion

## Ex Deorum 1.25
- Fixed crash with fluid containers
- Added `exdeorum:summation` result count type for sieve recipes, mostly for converting Ex Nihilo Sequentia recipes over (also usable for loot tables)
- It is now possible to define custom barrel, crucible, and sieve types. This may be useful for modpack authors who want to add enhanced compatibility with Ex Deorum. Documentation will be available soon.
- Fixed inconsistencies with barrel and sieve sounds

## Ex Deorum 1.24
- Fixed water crucibles filling infinitely with rainwater. (PR #54 by CPearl0)
- Add option (disabled by default) to make dirt from flowing water and witch water
- Fix Infested Leaves not showing in REI composite mode

## Ex Deorum 1.23
- Fixed rendering crash with crucibles
- Added recipes to upgrade meshes to the next tier

## Ex Deorum 1.22
- Fixed behavior of Porcelain Milk Bucket when the milk fluid is enabled
- Fixed barrel not rendering flat item contents like Magma Cream
- Fixed Infested Leaves not dropping any string or silkworms

## Ex Deorum 1.21
- Added Crook recipes. It is now possible to add drops to the crook, for example, you could make it so that using a Crook on tall grass would have a 1% chance of dropping a diamond.
- Added Crucible Heat Source recipes. Instead of using KubeJS, crucible heat sources can now be added with datapacks. Old KubeJS scripts will still work fine.
- Added Jade compatibility
- Added Roughly Enough Items compatibility to hide compat blocks for mods that aren't installed (use REI Plugin Compatibilities mod to gain full compatibility with Ex Deorum)
- Added configurable sifting interval to limit speed of sifting by hand in order to curb the speed of autoclickers on sieves. (PR #47 by CPearl0)
- Added Chinese translation (PR #45 by CPearl0)
- Fixed dedicated server crash when using Mycelium Spores to convert a cow into a Mooshroom (PR #44 by CPearl0)
- Fixed incorrect block lighting in JEI displays for Crucible heat sources and Crook recipes

## Ex Deorum 1.20
- Added Mechanical Hammer, a machine that uses FE to hammer blocks automatically. Uses 20 FE a tick by default and takes 200 ticks (10 seconds) to hammer an item with a hammer that has no efficiency.
- Fixed network bug when joining a server (PR #43 by CPearl0)

## Ex Deorum 1.19
- Fixed Cyclic pipes infinitely filling barrels when crafting fluid mixing recipes (ex. obsidian)
- Fixed network issue with fluid mixing recipes (PR #42 by CPearl0)

## Ex Deorum 1.18
- Fixed bug with milk and slime recipe.
- Barrel fluid mixing recipes can now consume the additive fluid using the `consumes_additive` boolean property in the recipe JSON. (ex. Water and Milk making slime)

## Ex Deorum 1.17
- Added Nether Wart as a sieve drop from Soul Sand
- Fixed Frozen Ocean biomes spawning floating icebergs in the void world type
- Fixed Slime Block barrel recipe not working in the barrel when a mod enables the Milk fluid
- Fixed duplication bug with top left player inventory slot in the Mechanical Sieve menu

## Ex Deorum 1.16
- Fixed barrels not correctly rendering their fluid contents when modified by pipes.
- Fixed Mechanical Sieve being instantly breakable with no tool. Now requires a pickaxe to destroy, like machines added by other mods.

## Ex Deorum 1.15
- Fixed not being able to enchant sieve meshes in the Enchanting Table.
- Fixed Barrels not rendering their contents properly
- Fixed Inventory Sorter by voxcpw voiding items when middle-clicking slots in the Mechanical Sieve GUI.
- Improved appearance of witch water to better match water so that the transformation animation looks smoother.

## Ex Deorum 1.14
- Added Mechanical Sieve, a machine that uses FE to sift blocks automatically. Uses 40 FE a tick by default and takes 100 ticks to sift an item with no efficiency enchantment.
- Added `by_hand_only` boolean field to Sieve recipes, which allows modpack makers to add sieve drops that don't drop from the Mechanical Sieve.
- Added JEI information telling the player that meshes can be enchanted with Fortune and Efficiency.
- Fixed minor rendering bug with infested leaves and Ars Nouveau leaves not rotating properly.
- Fixed bugged config option to disable Void World by default. Now, instead of being two options in the client and server configs, there is one option in the common config file that controls default world type both on the create world screen and in server.properties.
- Optimized syncing block entity visual updates from the server to the client.

## Ex Deorum 1.13
- Added new icon for JEI compost recipes to help differentiate from the other categories.
- Added `sieve_mesh` property to KubeJS's RecipeFilter, for usage in `RecipesEventJS.remove` to remove sieve recipes using a specific mesh. View the [updated documentation](https://exdeorum.readthedocs.io/en/latest).
- Fixed bug with Ex Deorum recipes not being removable by KubeJS with the OutputFilter and InputFilter.
- Fixed water crucible not displaying leaves properly.
- Fixed crimson nylium spores displaying as warped nylium in the water crucible.
- Fixed sieves not rendering AllTheCompressed blocks properly.
- Optimized crucible/sieve rendering of solid contents. No more Guava cache!

## Ex Deorum 1.12
- Added Wood Chippings, obtained by hammering logs. Usable as compost or as a crafting material for Sponges.
- Added some more KubeJS functions. Check out the [new documentation](https://exdeorum.readthedocs.io/en/latest) for Ex Deorum.
- Added options to the config to disable void generation in the Nether and End dimensions when using the Void World preset.
- Added recipe for Pointed Dripstone from hammering Dripstone.
- Fixed dupe bug with fluid mixing recipes
- Fixed bug where every fluid would appear as lava in a barrel
- Fixed minor bug with JEI heat sources not showing all usages of a block

## Ex Deorum 1.11
- Added support for NuclearCraft: Neoteric - Boron, Thorium, Lithium, and Magnesium ores
- Fixed bug where hoppers and other automation could not craft fluid mixing recipes in the barrel (ex. Water and Lava to make Obsidian)
- Fixed bug where JEI compatibility would fail to load if every mod compatible with Ex Deorum was also loaded (like in ATM9 mod pack)

## Ex Deorum 1.10
- Added crucibles, sieves, and barrels for wood types from Blue Skies and Aether mods.
- Added Certus Quartz Dust as a sieve drop from Dust.
- Allow extracting water from wooden crucibles using water bottles.
- Watering cans now accelerate crops from Pam's Harvestcraft 2 Crops
- Fixed a crash that happened with Ex Deorum barrels and crucibles when logging out of a world.
- Fixed infested leaves showing as invisible when using Oculus Shaders.
- It is now possible to see the melting rates for crucible heat sources in JEI.
- Barrels can now mix fluids with liquids placed on top of the (ex. water on top of a lava barrel creates obsidian)
- Optimized recipe lookups for the sieve.

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