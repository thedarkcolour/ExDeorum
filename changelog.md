## Ex Deorum 4.0
- Added Pale Oak Crucible, Sieve, Compressed Sieve, and Barrel
- Added Creaking Core to turn Pale Oak Log into Creaking Heart

## Ex Deorum 3.10
- Now requires KubeJS 7.2 to fix incompatibility (#158)

## Ex Deorum 3.9
- Add `#exdeorum:hammer_fortune_blacklist` and `#exdeorum:compressed_hammer_fortune_blacklist` item tags, allowing pack makers to prevent Fortune from affecting a block's hammer or compressed hammer drops

## Ex Deorum 3.8
- Fix memory leak in VisualUpdateTracker (#153)
- Added Hungarian translation (#147)
- Added Japanese translation (#152)

## Ex Deorum 3.7
- Fix Barrel Mixing recipes with a result size greater than 1 only giving one output
- Allow changing drops for Random Armor Trim and Pottery Sherd, also add Tide to possible trims (#133)

## Ex Deorum 3.6
- Implement custom Compressed Sieve types. Works the same as with sieves, just replace `sieve_materials` with `compressed_sieve_materials`
- Fix silkworms not applying to certain modded leaves like TFC

## Ex Deorum 3.5
- Remove Yellorium Dust sieve drop (#116)
- Fixed Fluid Transformation recipes requiring byproducts

## Ex Deorum 3.4
- Fix JEI bug with sieve recipes overflowing due to JEI API changes
- Fix invisible output slots on JEI crook recipes

## Ex Deorum 3.3
- Now built against Minecraft 1.21.1
- Add native EMI support.
- Fix bug where removing all Compressed Sieve recipes would break regular Sieve recipe display in JEI.
- Hack fix for random crashes with fluid transformation recipe cache

## Ex Deorum 3.2
- Fix KubeJS plugin.
- Buff wooden crucibles to 4x like in old Ex Nihilo

## Ex Deorum 3.1
- Fix bug where logs drop sawdust.

## Ex Deorum 3.0
- Update to NeoForge 1.21
- MODPACK MAKERS: Check the Ex Deorum Documentation soon, there have been several changes to Ex Deorum recipes.
- The One Probe (TOP) compat is unimplemented
- REI compat is unimplemented
- KubeJS compat is unimplemented
  #### Changes
  - Any recipes that have fluidstack outputs (ex. crucible recipes) must now change the `fluid` field to `id`, due to a change in NeoForge.
  - Any recipes that use fluid inputs have changed
  - Any recipes that used to output items now output item stacks instead (supports components!)
  - Any sieve/hammer recipes now return itemstacks instead of items. 
  - `exdeorum:silk_worm` now has ID `exdeorum:silkworm`, and `exdeorum:cooked_silk_worm` now has ID `exdeorum:cooked_silkworm`.