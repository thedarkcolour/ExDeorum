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
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.config.EConfig;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class WitchWaterBlock extends LiquidBlock {
    public WitchWaterBlock(Supplier<? extends FlowingFluid> pFluid, Properties pProperties) {
        super(pFluid.get(), pProperties);
    }

    @Override
    protected void entityInside(BlockState pState, Level level, BlockPos pPos, Entity entity, InsideBlockEffectApplier pEffectApplier, boolean pCanTriggerEffects) {
        if (!level.isClientSide()) {
            witchWaterEntityEffects(level, entity);
        }
    }

    // Only call on server
    public static void witchWaterEntityEffects(Level level, Entity entity) {
        if (entity.isAlive()) {
            var entityType = entity.getType();

            if (EConfig.SERVER.allowWitchWaterEntityConversion.get()) {
                if (entityType == EntityType.VILLAGER) {
                    var villager = (Villager) entity;

                    if (level.getDifficulty() != Difficulty.PEACEFUL) {
                        if (!villager.isBaby() && villager.getVillagerData().profession().is(VillagerProfession.CLERIC)) {
                            attemptToConvertEntity(level, villager, EntityType.WITCH);
                        } else {
                            villager.convertTo(EntityType.ZOMBIE_VILLAGER, ConversionParams.single(villager, false, false), EntitySpawnReason.CONVERSION, (ZombieVillager zombieVillager) -> {
                                zombieVillager.setVillagerData(villager.getVillagerData());
                                zombieVillager.setGossips(villager.getGossips().copy());
                                zombieVillager.setTradeOffers(villager.getOffers().copy());
                                zombieVillager.setVillagerXp(villager.getVillagerXp());
                            });
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
                    setVariant((MushroomCow) entity, "setVariant", MushroomCow.Variant.class, MushroomCow.Variant.BROWN);
                } else if (entityType == EntityType.AXOLOTL) {
                    setVariant((Axolotl) entity, "setVariant", Axolotl.Variant.class, Axolotl.Variant.BLUE);
                } else if (entityType == EntityType.RABBIT) {
                    setVariant((Rabbit) entity, "setVariant", Rabbit.Variant.class, Rabbit.Variant.EVIL);
                } else if (entityType == EntityType.PUFFERFISH) {
                    attemptToConvertEntity(level, entity, EntityType.GUARDIAN);
                } else if (entityType == EntityType.HORSE) {
                    if (level.getRandom().nextBoolean()) {
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
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 210));
            }
        }
    }

    @Nullable
    private static <T extends Mob> T attemptToConvertEntity(Level level, Entity entity, EntityType<T> newType) {
        if (level.getDifficulty() != Difficulty.PEACEFUL && entity instanceof Mob mob) {
            return mob.convertTo(newType, ConversionParams.single(mob, false, false), EntitySpawnReason.CONVERSION, converted -> {
                if (entity.hasCustomName()) {
                    converted.setCustomName(entity.getCustomName());
                    converted.setCustomNameVisible(entity.isCustomNameVisible());
                }

                converted.setPersistenceRequired();
            });
        }

        return null;
    }

    private static <T extends LivingEntity, V> void setVariant(T entity, String methodName, Class<V> variantType, V variant) {
        try {
            Method method = entity.getClass().getDeclaredMethod(methodName, variantType);
            method.setAccessible(true);
            method.invoke(entity, variant);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set entity variant for " + entity.getClass().getName(), e);
        }
    }
}
