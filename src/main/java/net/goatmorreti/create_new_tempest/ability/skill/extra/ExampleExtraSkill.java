package net.goatmorreti.create_new_tempest.ability.skill.extra;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.entity.magic.skill.WaterBladeProjectile;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.skill.CommonSkills;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;


/**
 * This Extra Skill is a held skill that makes you faster.
 * That is all it does.
 */
public class ExampleExtraSkill extends Skill {

    /**
     * This is where you define the path to the actual icon image.
     * @return
     */
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(CreateNewTempest.MOD_ID, "textures/skill/extra/example_extra.png");
    }

    // Here are some easy to change parameters to configure for the skill
    private final double skillCastCost = 10.0;     // How many magicules it costs to cast
    private final double epUnlockCost = 6000.0;   // EP Level required for unlocking the skill
    private final double learnCost = 100.0;           // When learning the skill, how hard is it. (Higher = harder). Default is 2.0

    public ExampleExtraSkill() {
        // Pass in the type of the skill here.
        super(SkillType.EXTRA);
    }

    /**
     * This function determines the unlock condtions for a skill. In this instance,
     * you simply need to have 6,000 EP
     *
     * @param entity - Player who has the skill
     * @param curEP - Current EP Value of the skill user
     * @return - True/False depending on if the player meets the conditions to obtain
     */
    public boolean meetEPRequirement(Player entity, double curEP) {
        return curEP > epUnlockCost;
    }

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) { return skillCastCost; }

    public double learningCost() { return learnCost; }

    /**
     * This method will allow the skill to be toggled when it has been mastered by the user.
     * @param instance
     * @param entity
     * @return
     */
    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isMastered(entity);
    }

    /**
     * The tick method is controlled by this, and so we only tick if we have mastered
     * the skill, and it is turned on. This is different from being held
     * @param instance
     * @param entity
     * @return
     */
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return !instance.isMastered(entity) ? false : instance.isToggled();
    }

    /**
     * This is the method called when the ability is in the ability bar,
     * and the corresponding ability key is pressed. You can put anything you want in here.
     *
     * @param instance
     * @param entity
     */
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        // Here we just make a little sound for the speed effect
        entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    /**
     * This method is ran while the ability key is being held down explicitly.
     *
     * @param instance
     * @param entity
     * @param heldTicks
     * @return True/False whether the ability should remain active
     */
    public boolean onHeld(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        // Every 20 ticks (1 second) check if the player is out of magicules
        if (heldTicks % 20 == 0 && SkillHelper.outOfMagicule(entity, instance)) {
            return false;
        } else { // Otherwise, ever 60 ticks (3 seconds) increase the mastery
            if (heldTicks % 60 == 0 && heldTicks > 0) {
                this.addMasteryPoint(instance, entity);
            }

            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, instance.isMastered(entity) ? 2 : 1, false, false, false));
            return true;
        }
    }

    /**
     * Whenever we let go, we need to disable the speed effect and give some sign that it is done. Like a sound
     * @param instance
     * @param entity
     * @param heldTicks
     */
    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (!instance.isToggled()) {
            if (entity.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                entity.removeEffect(MobEffects.MOVEMENT_SPEED);
                entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    /**
     * This onTick method is for when the skill is toggled on. All we need to do is apply the effect
     * unless the user is out of magicules.
     * @param instance
     * @param entity
     */
    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
        if (SkillHelper.outOfMagicule(entity, this.magiculeCost(entity, instance) * 5.0)) {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                player.displayClientMessage(Component.translatable("tensura.skill.lack_magicule.toggled_off", new Object[]{instance.getSkill().getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
            }

            instance.setToggled(false);
        } else {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0, false, false, false));
        }
    }

    /**
     * When we toggle on, we need to apply the effect, and make it tick. Also play a little sound for the first time.
     * Basically, anything in here will happen once for the duration of the skill.
     * @param instance
     * @param entity
     */
    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        this.onTick(instance, entity);
        entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    /**
     * When we toggle it off, make sure you remove the effect from the player.
     * Any other "final" events can go in here.
     * @param instance
     * @param entity
     */
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            entity.removeEffect(MobEffects.MOVEMENT_SPEED);
            entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }







}
