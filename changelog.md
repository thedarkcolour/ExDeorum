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