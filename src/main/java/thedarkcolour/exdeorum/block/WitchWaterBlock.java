/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.config.EConfig;

import java.util.function.Supplier;

public class WitchWaterBlock extends LiquidBlock {
    public WitchWaterBlock(Supplier<? extends FlowingFluid> pFluid, Properties pProperties) {
        super(pFluid, pProperties);
    }

    @Override
    public void entityInside(BlockState pState, Level level, BlockPos pPos, Entity entity) {
        if (!level.isClientSide && entity.isAlive()) {
            var entityType = entity.getType();

            if (EConfig.SERVER.allowWitchWaterEntityConversion.get()) {
                if (entityType == EntityType.VILLAGER) {
                    var villager = (Villager) entity;

                    if (level.getDifficulty() != Difficulty.PEACEFUL) {
                        if (!villager.isBaby() && villager.getVillagerData().getProfession() == VillagerProfession.CLERIC) {
                            if (attemptToConvertEntity(level, villager, EntityType.WITCH) != null) {
                                villager.releaseAllPois();
                            }
                        } else {
                            var zombieVillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                            if (zombieVillager != null) {
                                zombieVillager.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(zombieVillager.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), null);
                                zombieVillager.setVillagerData(villager.getVillagerData());
                                zombieVillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE));
                                zombieVillager.setTradeOffers(villager.getOffers().createTag());
                                zombieVillager.setVillagerXp(villager.getVillagerXp());

                                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(villager, zombieVillager);

                                villager.discard();
                            }
                        }
                    }
                } else if (entityType == EntityType.SKELETON) {
                    attemptToConvertEntity(level, entity, EntityType.WITHER_SKELETON);
                } else if (entityType == EntityType.CREEPER) {
                    entity.getEntityData().set(Creeper.DATA_IS_POWERED, true);
                } else if (entityType == EntityType.SPIDER) {
                    attemptToConvertEntity(level, entity, EntityType.CAVE_SPIDER);
                } else if (entityType == EntityType.SQUID) {
                    attemptToConvertEntity(level, entity, EntityType.GHAST);
                } else if (entityType == EntityType.PIG || entityType == EntityType.PIGLIN) {
                    attemptToConvertEntity(level, entity, EntityType.ZOMBIFIED_PIGLIN);
                } else if (entityType == EntityType.HOGLIN) {
                    attemptToConvertEntity(level, entity, EntityType.ZOGLIN);
                } else if (entityType == EntityType.MOOSHROOM) {
                    ((MushroomCow) entity).setVariant(MushroomCow.MushroomType.BROWN);
                } else if (entityType == EntityType.AXOLOTL) {
                    ((Axolotl) entity).setVariant(Axolotl.Variant.BLUE);
                } else if (entityType == EntityType.RABBIT) {
                    ((Rabbit) entity).setVariant(Rabbit.Variant.EVIL);
                } else if (entityType == EntityType.PUFFERFISH) {
                    attemptToConvertEntity(level, entity, EntityType.GUARDIAN);
                } else if (entityType == EntityType.HORSE) {
                    if (level.random.nextBoolean()) {
                        attemptToConvertEntity(level, entity, EntityType.ZOMBIE_HORSE);
                    } else {
                        attemptToConvertEntity(level, entity, EntityType.SKELETON_HORSE);
                    }
                }
            }

            if (entityType == EntityType.PLAYER) {
                var living = (LivingEntity) entity;
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 210));
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 210, 2));
                living.addEffect(new MobEffectInstance(MobEffects.WITHER, 210));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 210));
            }
        }
    }

    @Nullable
    private static <T extends Mob> T attemptToConvertEntity(Level level, Entity entity, EntityType<T> newType) {
        if (level.getDifficulty() != Difficulty.PEACEFUL && entity instanceof LivingEntity) {
            var newEntity = newType.create(level);

            if (newEntity != null) {
                var serverLevel = (ServerLevelAccessor) level;
                newEntity.copyPosition(entity);
                ForgeEventFactory.onFinalizeSpawn(newEntity, serverLevel, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.CONVERSION, null, null);
                newEntity.setNoAi(newEntity.isNoAi());

                if (entity.hasCustomName()) {
                    newEntity.setCustomName(entity.getCustomName());
                    newEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }

                newEntity.setPersistenceRequired();
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) entity, newEntity);
                serverLevel.addFreshEntityWithPassengers(newEntity);
                entity.discard();
            }

            return newEntity;
        }

        return null;
    }
}
