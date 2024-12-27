package net.goatmorreti.create_new_tempest.race.sculk;

import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.race.vampire.VampireRace;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import net.goatmorreti.create_new_tempest.registry.skill.AllSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class MechanicalColossusRace extends Race {

    public MechanicalColossusRace() {
        super(Difficulty.INTERMEDIATE);
    }

    public double getBaseHealth() {
        return (double)26.0F;
    }

    public float getPlayerSize() {
        return 2.5F;
    }

    public double getBaseAttackDamage() {
        return (double)1.0F;
    }

    public double getBaseAttackSpeed() {
        return (double)4.0F;
    }

    public double getKnockbackResistance() {
        return (double)1.0F;
    }

    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer();
    }

    public double getMovementSpeed() {
        return 2.0;
    }

    public double getSprintSpeed() {return 0.26;}

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(10.0, 100.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(1500.0, 3000.0);
    }

    @Override
    public boolean isMajin() {
        return false;
    }

    @Override
    public double getSpiritualHealthMultiplier() {
        return 2.0;
    }

    @Override
    public double getAdditionalSpiritualHealth() {
        return 20.0;
    }

    @Override
    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> skills = new ArrayList<>();
        skills.add(AllSkills.MECHANICAL_EYE_SKILL.get());
        skills.add(AllSkills.MECHANICAL_HANDS_SKILL.get());
        return skills;
    }

    @Override
    public List<Race> getNextEvolutions(Player player) {
        List<Race> evolutions = new ArrayList<>();
        // Define future evolutions if applicable
        return evolutions;
    }
    public void raceTick(Player player) {
        player.addEffect(new MobEffectInstance((MobEffect) TensuraMobEffects.CORROSION.get(), 40, 2, false, false, false));
    }
}
