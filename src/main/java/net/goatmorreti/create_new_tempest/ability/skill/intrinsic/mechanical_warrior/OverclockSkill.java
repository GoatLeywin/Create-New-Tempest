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
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        return 0.0; // Assuming no magicule cost
    }

    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        return entity.hasEffect(AllEffects.OVERCLOCK.get());
    }

    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        entity.removeEffect(AllEffects.OVERCLOCK.get());
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!this.failedToActivate(player, AllEffects.OVERCLOCK.get())) {
                if (!player.hasEffect(AllEffects.OVERCLOCK.get())) {
                    if (SkillHelper.outOfMagicule(player, instance)) {
                        return;
                    }

                    this.addMasteryPoint(instance, player);
                    instance.setCoolDown(1200); // Cooldown of 1200 ticks (1 minute)
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F); // Change to appropriate sound
                    player.addEffect(new MobEffectInstance((MobEffect)AllEffects.OVERCLOCK.get(), this.isMastered(instance, player) ? 7200 : 3600, 0, false, false, false));

                    // Add particles around the player
                    TensuraParticleHelper.addServerParticlesAroundSelf(player, ParticleTypes.ENCHANT);
                    TensuraParticleHelper.spawnServerParticles(player.level,
                            (ParticleOptions) TensuraParticles.LIGHTNING_SPARK.get(),
                            player.getX(), player.getY(), player.getZ(),
                            55, 0.08, 0.08, 0.08, 0.5, true);
                } else {
                    player.removeEffect(AllEffects.OVERCLOCK.get());
                    player.playSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F); // Change to appropriate sound
                }
            }
        }
    }
}

