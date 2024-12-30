package net.goatmorreti.create_new_tempest.ability.skill.intrinsic;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.simibubi.create.AllSoundEvents;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MechanicalBodySkill extends Skill {
    public MechanicalBodySkill() {
        super(SkillType.INTRINSIC);
    }

    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(CreateNewTempest.MOD_ID, "textures/skill/intrinsic/mechanical_eye.png");
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
        // Collect all nearby LivingEntity UUIDs within a 30-block radius that match the criteria
        Collection<String> uuidList = entity.getLevel()
                .getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(30.0F),
                        target -> this.isNamed(entity, target))
                .stream()
                .map(Entity::getStringUUID)
                .collect(Collectors.toList());

        CompoundTag tag = instance.getOrCreateTag();

        // Check if the "UnyieldingList" tag exists
        if (tag.contains("UnyieldingList")) {
            CompoundTag list = tag.getCompound("UnyieldingList");
            if (list == null) {
                return;
            }

            // Create a copy of the keys in the list to iterate safely
            List<String> keyList = List.copyOf(list.getAllKeys());
            if (!keyList.isEmpty()) {
                for (String key : keyList) {
                    // Check if the entity is still nearby
                    if (uuidList.contains(key)) {
                        // Increase the counter if the entity is still in range
                        list.putInt(key, list.getInt(key) + (instance.isMastered(entity) ? 2 : 1));
                        uuidList.remove(key);
                    } else {
                        // Decrease the counter if the entity is no longer in range
                        int point = list.getInt(key) - 1;
                        if (point <= 0) {
                            // Remove the entity from the list if its counter reaches zero
                            list.remove(key);
                        } else {
                            list.putInt(key, point);
                        }
                    }
                }
            }

            // Add new entities to the list
            if (!uuidList.isEmpty()) {
                for (String key : uuidList) {
                    list.putInt(key, Math.min(list.getInt(key) + 10, 1200)); // Max value capped at 1200
                }
            }

            // Mark the instance as dirty to indicate data has changed
            instance.markDirty();
        } else if (!uuidList.isEmpty()) {
            // Create a new list if none exists and populate it with initial values
            CompoundTag list = new CompoundTag();
            for (String uuid : uuidList) {
                list.putInt(uuid, 1);
            }
            tag.put("UnyieldingList", list);
            instance.markDirty();
        }
    }

    private CloneEntity spawnBackup(ManasSkillInstance instance, LivingEntity entity, boolean copyEquipment) {
        Level level = entity.level;
        CompoundTag tag = instance.getOrCreateTag();

        double EP = TensuraEPCapability.getEP(entity);
        EntityType<CloneEntity> type = entity.isBaby() ? TensuraEntityTypes.CLONE_SLIM.get() : TensuraEntityTypes.CLONE_DEFAULT.get();
        CloneEntity clone = new CloneEntity(type, level);
        level.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                AllSoundEvents.CONFIRM.getMainEvent(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
        if (entity instanceof Player player) {
            //clone.initializeForPlayer(player);
        }

        clone.setSkill(this);
        clone.setImmobile(true);
        clone.setChunkLoader(true);
        clone.setHealth(entity.getHealth());
        if (copyEquipment) {
            clone.copyEquipments(entity);
        }

        clone.copyStatsAndSkills(entity, CloneEntity.CopySkill.NONE, true);
        CloneEntity.copyEffects(entity, clone);
        TensuraEPCapability.setLivingEP(clone, EP);
        clone.setPos(entity.position());
        clone.loadChunkHandler();
        CloneEntity.copyRotation(entity, clone);
        level.addFreshEntity(clone);
        tag.putUUID("Backup", clone.getUUID());
        instance.markDirty();

        return clone;
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        Level level = entity.level;
        if (level instanceof ServerLevel serverLevel) {
            CompoundTag tag = instance.getOrCreateTag();
            double maxMP = entity.getAttributeValue(TensuraAttributeRegistry.MAX_MAGICULE.get());

            if (!tag.contains("Backup")) {
                if (!SkillHelper.outOfMagicule(entity, maxMP * 0.25)) {
                    this.spawnBackup(instance, entity, false);
                    instance.setCoolDown(instance.isMastered(entity) ? 3 : 5);
                }
            } else {
                UUID uuid = tag.getUUID("Backup");
                Entity existingEntity = SkillHelper.getEntityFromUUID(serverLevel, uuid, CloneEntity.class::isInstance);

                if (existingEntity instanceof CloneEntity backup) {
                    if (entity.isDeadOrDying()) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(
                                    Component.translatable("tensura.skill.mode.unyielding.backup_remove")
                                            .withStyle(ChatFormatting.RED),
                                    true
                            );
                        }
                        backup.remove();
                    } else if (backup.level != entity.level && !instance.isMastered(entity)) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(
                                    Component.translatable("tensura.skill.mode.unyielding.backup_different_dimension")
                                            .withStyle(ChatFormatting.RED),
                                    true
                            );
                        }
                    } else if (backup.isAlive()) {
                        instance.setCoolDown(instance.isMastered(entity) ? 3 : 5);
                        serverLevel.playSound(
                                null,
                                entity.getX(),
                                entity.getY(),
                                entity.getZ(),
                                AllSoundEvents.WRENCH_ROTATE.getMainEvent(),
                                SoundSource.PLAYERS,
                                1.0F,
                                1.0F
                        );

                        CloneEntity newClone = this.spawnBackup(instance, entity, true);
                        newClone.setUUID(entity.getUUID());
                        SkillHelper.moveAcrossDimensionTo(entity, backup);
                        backup.copyEquipmentsOntoOwner(entity, true);
                        CloneEntity.copyEffects(backup, entity);
                        entity.setHealth(backup.getHealth());
                        backup.remove(Entity.RemovalReason.DISCARDED);
                    }
                } else {
                    if (!SkillHelper.outOfMagicule(entity, maxMP * 0.25)) {
                        this.spawnBackup(instance, entity, false);
                        instance.setCoolDown(instance.isMastered(entity) ? 3 : 5);
                    }
                }
            }
        }
    }

    private boolean isNamed(LivingEntity owner, LivingEntity target) {
        // Example logic: Check if the target has the same name as the owner
        if (target.getCustomName() != null && owner.getCustomName() != null) {
            return target.getCustomName().getString().equals(owner.getCustomName().getString());
        }

        // Add any additional checks if needed, such as specific entity types or attributes
        return false;
    }

}
