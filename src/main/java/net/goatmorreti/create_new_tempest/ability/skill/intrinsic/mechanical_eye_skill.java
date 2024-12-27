package net.goatmorreti.create_new_tempest.ability.skill.intrinsic;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class mechanical_eye_skill extends Skill {

    private final double skillCastCost = 10.0;
    private final double epUnlockCost = 5000.0;
    private final double learnCost = 50.0;
    private final Map<Player, Predicate<Player>> activePredicates = new HashMap<>();

    public mechanical_eye_skill() {
        super(SkillType.INTRINSIC);
    }

    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(CreateNewTempest.MOD_ID, "textures/skill/intrinsic/mechanical_eye.png");
    }

    public boolean meetEPRequirement(Player entity, double curEP) {
        return curEP >= epUnlockCost;
    }

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        return skillCastCost;
    }

    public double learningCost() {
        return learnCost;
    }

    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isToggled() && instance.isMastered(entity);
    }

    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            PlayerPredicateWrapper wrapper = new PlayerPredicateWrapper(player);
            GogglesItem.addIsWearingPredicate(wrapper);
            activePredicates.put(player, wrapper);
            wrapper.setActive(true); // Activate the predicate

            player.displayClientMessage(Component.translatable("Mechanical Eyes activated")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), true);
            player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), AllSoundEvents.CONFIRM.getMainEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            PlayerPredicateWrapper wrapper = (PlayerPredicateWrapper) activePredicates.remove(player);
            if (wrapper != null) {
                wrapper.setActive(false); // Deactivate the predicate
            }

            player.displayClientMessage(Component.translatable("Mechanical Eyes deactivated")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), AllSoundEvents.DENY.getMainEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (SkillHelper.outOfMagicule(player, skillCastCost * 5)) {
                instance.setToggled(false);
                onToggleOff(instance, player); // Properly toggle off the skill
            }
        }
    }

    /**
     * Wrapper class for a player predicate with an active flag.
     */
    private static class PlayerPredicateWrapper implements Predicate<Player> {
        private final Player player;
        private boolean active;

        public PlayerPredicateWrapper(Player player) {
            this.player = player;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean test(Player p) {
            return active && p.equals(player);
        }
    }
}
