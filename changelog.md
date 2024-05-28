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