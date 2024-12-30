package net.goatmorreti.create_new_tempest.effect;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.effect.template.SkillMobEffect;
import com.github.manasmods.tensura.effect.template.Transformation;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class OverclockEffect extends SkillMobEffect implements Transformation {
    protected static final UUID OVERCLOCK_UUID = UUID.fromString("37dfcf74-cb78-4979-88c0-f69644047926");
    protected static final UUID ENERGY_UUID = UUID.fromString("eee53144-e5b1-4220-9d95-f4e615f3b6c5");

    public OverclockEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MAX_HEALTH, OVERCLOCK_UUID.toString(), 20.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, OVERCLOCK_UUID.toString(), 15.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, OVERCLOCK_UUID.toString(), 0.05, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(TensuraAttributeRegistry.MAX_MAGICULE.get(), ENERGY_UUID.toString(), 1.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(TensuraAttributeRegistry.MAX_AURA.get(), ENERGY_UUID.toString(), 1.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);

        // Emit both black and red particles every tick
        //TensuraParticleHelper.addParticlesAroundSelf(entity, TensuraParticles.BLACK_LIGHTNING_SPARK.get());
        TensuraParticleHelper.addParticlesAroundSelf(entity, TensuraParticles.DARK_RED_LIGHTNING_SPARK.get());

        if (entity instanceof Player player) {
            TensuraPlayerCapability.getFrom(player).ifPresent(cap -> {
                double maxMagicule = player.getAttributeValue(TensuraAttributeRegistry.MAX_MAGICULE.get());
                cap.setMagicule(maxMagicule);
                double maxAura = player.getAttributeValue(TensuraAttributeRegistry.MAX_AURA.get());
                cap.setAura(maxAura);
            });

            TensuraPlayerCapability.sync(player);
            TensuraEPCapability.updateEP(player);
        }
    }

    public void onApplied(LivingEntity entity, int amplifier) {
        // Emit both black and red particles upon effect application
        TensuraParticleHelper.addParticlesAroundSelf(entity, TensuraParticles.BLACK_LIGHTNING_SPARK.get());
        TensuraParticleHelper.addParticlesAroundSelf(entity, TensuraParticles.DARK_RED_LIGHTNING_SPARK.get());
    }

    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        if (entity instanceof Player player) {
            TensuraPlayerCapability.getFrom(player).ifPresent(cap -> {
                cap.setMagicule(cap.getMagicule() / 2.0); // Halve mechanical energy on effect end
                cap.setAura(cap.getAura() / 2.0);         // Halve aura on effect end
                TensuraPlayerCapability.sync(player);
            });

            TensuraEPCapability.updateEP(entity);

            if (!player.isSpectator() && !player.isCreative()) {
                if (player.getAbilities().invulnerable) {
                    player.getAbilities().invulnerable = false;
                    player.getAbilities().mayfly = false;
                    player.onUpdateAbilities();
                }
            }
        }
    }

    
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }

    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return modifier.getId().equals(ENERGY_UUID)
                ? modifier.getAmount()
                : modifier.getAmount() * (amplifier + 1);
    }
}
