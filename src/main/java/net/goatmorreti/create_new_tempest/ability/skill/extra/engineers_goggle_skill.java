package net.goatmorreti.create_new_tempest.ability.skill.extra;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.Skill;
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

import java.util.function.Predicate;

public class engineers_goggle_skill extends Skill {

    private final double skillCastCost = 10.0;
    private final double epUnlockCost = 5000.0;
    private final double learnCost = 50.0;

    public engineers_goggle_skill() {
        super(SkillType.EXTRA);
    }

    @Override
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(CreateNewTempest.MOD_ID, "textures/skill/extra/engineers_goggle_skill.png");
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
        return true;
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isToggled() && instance.isMastered(entity);
    }

    @Override
    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            GogglesItem.addIsWearingPredicate(createPlayerPredicate(player));
            player.displayClientMessage(Component.translatable("skill.engineers_goggle.activated")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), true);
            player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            GogglesItem.addIsWearingPredicate(p -> false); // Remove the hook
            player.displayClientMessage(Component.translatable("skill.engineers_goggle.deactivated")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (SkillHelper.outOfMagicule(player, skillCastCost * 5)) {
                instance.setToggled(false);
                player.displayClientMessage(Component.translatable("skill.engineers_goggle.lack_magicule")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                GogglesItem.addIsWearingPredicate(p -> false); // Remove the hook
            }
        }
    }

    private static Predicate<Player> createPlayerPredicate(Player player) {
        return p -> p.equals(player); // Ensure the predicate only applies to the specific player
    }
}
