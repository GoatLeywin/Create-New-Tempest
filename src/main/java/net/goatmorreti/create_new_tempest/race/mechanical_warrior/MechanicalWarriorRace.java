package net.goatmorreti.create_new_tempest.race.mechanical_warrior;

import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import net.goatmorreti.create_new_tempest.config.CreateNewTempestConfig;
import net.goatmorreti.create_new_tempest.registry.race.AllRaces;
import net.goatmorreti.create_new_tempest.registry.skill.AllSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

public class MechanicalWarriorRace extends Race {

    public MechanicalWarriorRace() {
        super(Difficulty.INTERMEDIATE);
    }

    public double getBaseHealth() {
        return (double)40.0F;
    }

    public float getPlayerSize() {
        return 2.0F;
    }

    public double getBaseAttackDamage() {
        return (double)1.2F;
    }

    public double getBaseAttackSpeed() {
        return (double)4.6F;
    }

    public double getKnockbackResistance() {
        return (double)0.5F;
    }

    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer();
    }

    public double getMovementSpeed() {
        return 0.2;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(3000.0, 4000.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(1500.0, 3500.0);
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
        //skills.add(AllSkills.OVERCLOCK_SKILL.get());
        return skills;
    }

    @Override
    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        //list.add((Race)((IForgeRegistry) TensuraRaces.RACE_REGISTRY.get()).getValue(AllRaces.MECHANICAL_COLOSSUS));
        return list;
    }

    public double getEvolutionPercentage(Player player) {
        return TensuraPlayerCapability.getBaseEP(player) * (double)100.0F / (Double) CreateNewTempestConfig.INSTANCE.racesConfig.epToMechanicalWarrior.get();
    }

    public void raceTick(Player player) {
        if (player.isInWater()) {
            // Apply Corrosion I when the player is in water
            player.addEffect(new MobEffectInstance((MobEffect) TensuraMobEffects.CORROSION.get(), 40, 1, false, false, false));
        }

        if (player.level.isRainingAt(player.blockPosition())) {
            // Apply Paralysis I when the player is in rain, but not in water
            player.addEffect(new MobEffectInstance((MobEffect) TensuraMobEffects.PARALYSIS.get(), 40, 0, false, false, false));
        }
    }
}