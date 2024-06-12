## Ex Deorum 2.10
- Fix issue with Inventory Tweaks item restocking for sieves
- Fix sieving not using more than 64 sieves at a time when high sieve ranges were enabled
- Fix desync bug with barrels that sometimes happens when emptying/filling with a bucket
- Fix overriding Superflat world type
- Fix lag spike when inserting compost into the Barrel for the first time
- Fix June barrel config
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

## Ex Deorum 2.9
- Fix overriding default world type in Create World screen when common config option set_void_world_as_default is false.
- Removed set_void_world_as_default option from client config, you must now only use the option from the common config.
- Fixed bug where barrels filled by rain would not trigger a transformation recipe (e.g. Witch Water conversion)
- Added clientside config option to disable rainbow compost in barrels during June
- Fixed barrel fluid transformation recipes ignoring the result fluid and only crafting witch water
- Fixed bug where barrels would not trigger a transformation recipe while it is raining (ex. Witch Water conversion)
- Add config options to prevent barrels and crucibles from collecting rainwater

## Ex Deorum 2.8
- Fixed bug where compressed sieves would not drop their sieves upon being broken.
- Fix typo in JEI integration where both sieve categories were "Compressed Sieve".
- Add integration for SkyBlock Builder. Now, if that mod is installed, SkyBlock builder's preset is chosen by default instead of Ex Deorum's. This goes for the server.properties file too.
- Added config option for using compressed sieves simultaneously.
- Added Immersive Engineering integration for Ex Deorum's ore chunks.

## Ex Deorum 2.7
- Fixed End Portal not spawning and End Cities not generating
- Fixed an issue with void worlds not generating bastion remnants, nether fortresses, or obsidian pillars in the End

## Ex Deorum 2.6
- Fixed crash on server load

## Ex Deorum 2.5
- Fixed compatibility with EMI
- Add compressed hammers, sieves, and blocks from Ex Deorum 1.30

## Ex Deorum 2.4
- Added compatibility with EMI (only works when also using JEI)

## Ex Deorum 2.3
- Fixed infested leaves not dropping string with a Crook

## Ex Deorum 2.2
- Fixed hammers and crooks not working

## Ex Deorum 2.1
- Add a config option to disable the automated_sieve nerf (now you can enable machines to use multiple sieves simultaneously)

## Ex Deorum 2.0
- Ported to NeoForge 1.20.4
- Has same features as Ex Deorum 1.28
- Fixed light levels for barrel and crucible