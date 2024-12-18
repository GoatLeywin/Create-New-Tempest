package net.goatmorreti.create_new_tempest.ability.skill.extra;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.Skill;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class engineers_goggle_skill extends Skill {

    private final double skillCastCost = 10.0; // Magicule cost per tick
    private final double epUnlockCost = 5000.0; // EP needed to unlock the skill
    private final double learnCost = 50.0; // Learning difficulty

    public engineers_goggle_skill() {
        super(SkillType.EXTRA);
    }

    @Override
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(CreateNewTempest.MOD_ID, "textures/skill/extra/example_extra.png");
    }

    @Override
    public boolean meetEPRequirement(Player entity, double curEP) {
        return curEP >= epUnlockCost;
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        return skillCastCost;
    }

    @Override
    public double learningCost() {
        return learnCost;
    }

    @Override
    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isMastered(entity); // Allow toggling only if mastered
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isToggled(); // Tick only if the skill is toggled on
    }

    @Override
    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            addGoggleTag(player);
            player.displayClientMessage(Component.translatable("skill.engineers_goggle.activated")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), true);
            player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            removeGoggleTag(player);
            player.displayClientMessage(Component.translatable("skill.engineers_goggle.deactivated")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CONDUIT_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (SkillHelper.outOfMagicule(player, skillCastCost)) {
                // If out of magicules, toggle off the skill
                instance.setToggled(false);
                onToggleOff(instance, player);
                player.displayClientMessage(Component.translatable("skill.engineers_goggle.lack_magicule")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            }
        }
    }

    private void addGoggleTag(Player player) {
        // Add the "goggle" tag to the player's persistent data
        player.getPersistentData().putBoolean("create.goggle", true);
    }

    private void removeGoggleTag(Player player) {
        // Remove the "goggle" tag from the player's persistent data
        player.getPersistentData().remove("create.goggle");
    }
}
