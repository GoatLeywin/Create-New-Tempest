package net.goatmorreti.create_new_tempest.race.mechanical_scholar;

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

public class MechanicalScholarRace extends Race {

    public MechanicalScholarRace() {super(Difficulty.INTERMEDIATE);}

    public double getBaseHealth() {
        return (double)26.0F;
    }

    public float getPlayerSize() {
        return 2.0F;
    }

    public double getBaseAttackDamage() {
        return (double)1.0F;
    }

    public double getBaseAttackSpeed() {
        return (double)4.0F;
    }

    public double getKnockbackResistance() {
        return (double)0.5F;
    }

    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer();
    }

    public double getMovementSpeed() {
        return 0.1;
    }

    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(10.0, 100.0);
    }

    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(7500.0, 9000.0);
    }

    public boolean isMajin() {
        return true;
    }

    public double getSpiritualHealthMultiplier() {
        return 10.0;
    }

    public double getAdditionalSpiritualHealth() {
        return 50.0;
    }

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> skills = new ArrayList<>();
        //skills.add(AllSkills.MECHANICAL_EYE_SKILL.get());
        return skills;
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        //list.add((Race)((IForgeRegistry) TensuraRaces.RACE_REGISTRY.get()).getValue(AllRaces.MECHANICAL_GIANT));
        return list;
    }

    public double getEvolutionPercentage(Player player) {
        return TensuraPlayerCapability.getBaseEP(player) * (double)100.0F / (Double) CreateNewTempestConfig.INSTANCE.racesConfig.epToMechanicalScholar.get();
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

