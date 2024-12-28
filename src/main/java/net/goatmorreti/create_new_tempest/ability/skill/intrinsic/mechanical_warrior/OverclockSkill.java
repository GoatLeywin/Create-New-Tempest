package net.goatmorreti.create_new_tempest.ability.skill.intrinsic.mechanical_warrior;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.ability.skill.Skill.SkillType;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.effect.template.Transformation;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import net.goatmorreti.create_new_tempest.registry.effect.AllEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class OverclockSkill extends Skill implements Transformation {
    public OverclockSkill() {
        super(SkillType.INTRINSIC);
    }

    @Override
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        return 0.0; // Assuming no magicule cost
    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        return entity.hasEffect(AllEffects.OVERCLOCK.get());
    }

    @Override
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        entity.removeEffect(AllEffects.OVERCLOCK.get());
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        if (!this.failedToActivate(entity, AllEffects.OVERCLOCK.get())) {
            if (!entity.hasEffect(AllEffects.OVERCLOCK.get())) {
                if (SkillHelper.outOfMagicule(entity, instance)) {
                    return;
                }

                this.addMasteryPoint(instance, entity);
                instance.setCoolDown(1200); // Cooldown of 1200 ticks (1 minute)
                entity.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F); // Change to appropriate sound
                entity.addEffect(new MobEffectInstance(AllEffects.OVERCLOCK.get(),
                        this.isMastered(instance, entity) ? 7200 : 3600, 0, false, false, false));

                // Add particles around the entity
                TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.ENCHANT);
                TensuraParticleHelper.spawnServerParticles(entity.level,
                        (ParticleOptions) TensuraParticles.LIGHTNING_SPARK.get(),
                        entity.getX(), entity.getY(), entity.getZ(),
                        55, 0.08, 0.08, 0.08, 0.5, true);
            } else {
                entity.removeEffect(AllEffects.OVERCLOCK.get());
                entity.playSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F); // Change to appropriate sound
            }
        }
    }
}

