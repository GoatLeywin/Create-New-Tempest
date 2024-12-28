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
    // Constants for Overclock effect modifiers
    protected static final UUID OVERCLOCK_UUID = UUID.fromString("5e6fe67e-4a06-11ee-be56-0242ac120002");
    protected static final UUID ENERGY_UUID = UUID.fromString("0a92e00e-79ec-11ee-b962-0242ac120002");

    public OverclockEffect(MobEffectCategory category, int color) {
        super(category, color);
        // Adding attribute modifiers to reflect mechanical overclocking
        this.addAttributeModifier(Attributes.MAX_HEALTH, OVERCLOCK_UUID.toString(), 20.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, OVERCLOCK_UUID.toString(), 15.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, OVERCLOCK_UUID.toString(), 0.05, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(TensuraAttributeRegistry.MAX_MAGICULE.get(), ENERGY_UUID.toString(), 1.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(TensuraAttributeRegistry.MAX_AURA.get(), ENERGY_UUID.toString(), 1.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity instanceof Player player) {
            // Synchronize mechanical energy and aura stats
            TensuraPlayerCapability.getFrom(player).ifPresent(cap -> {
                double maxMagicule = player.getAttributeValue(TensuraAttributeRegistry.MAX_MAGICULE.get());
                cap.setMagicule(maxMagicule);
                double maxAura = player.getAttributeValue(TensuraAttributeRegistry.MAX_AURA.get());
                cap.setAura(maxAura);
            });

            TensuraPlayerCapability.sync(player);
            TensuraEPCapability.updateEP(player);

            // Activate Overclock-specific abilities
            if (amplifier >= 1) {
                if (player.isSpectator() || player.isCreative()) {
                    return;
                }

                if (!player.getAbilities().invulnerable) {
                    player.getAbilities().invulnerable = true; // Overclocking grants invulnerability
                    player.getAbilities().mayfly = true;       // Overclocking enables flight
                    player.onUpdateAbilities();
                }
            }
        }
    }

    public void onApplied(LivingEntity entity, int amplifier) {
        if (!this.failedToActivate(entity, this)) {
            ParticleOptions particle = amplifier >= 1
                    ? TensuraParticles.DARK_RED_LIGHTNING_SPARK.get() // Red sparks for Overclock activation
                    : TensuraParticles.BLACK_LIGHTNING_SPARK.get();   // Default sparks
            TensuraParticleHelper.addParticlesAroundSelf(entity, particle);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        if (entity instanceof Player player) {
            TensuraPlayerCapability.getFrom(player).ifPresent(cap -> {
                cap.setMagicule(cap.getMagicule() / 2.0); // Halve mechanical energy on effect end
                cap.setAura(cap.getAura() / 2.0);         // Halve aura on effect end
                TensuraPlayerCapability.sync(player);
            });

            TensuraEPCapability.updateEP(entity);

            // Reset abilities on effect removal
            if (!player.isSpectator() && !player.isCreative()) {
                if (player.getAbilities().invulnerable) {
                    player.getAbilities().invulnerable = false; // Remove invulnerability
                    player.getAbilities().mayfly = false;      // Remove flight ability
                    player.onUpdateAbilities();
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Overclock pulses more frequently than typical effects
        return duration % 5 == 0;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return modifier.getId().equals(ENERGY_UUID)
                ? modifier.getAmount()
                : modifier.getAmount() * (amplifier + 1);
    }
}
