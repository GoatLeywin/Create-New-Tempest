package net.goatmorreti.create_new_tempest.effect;

import com.github.manasmods.manascore.attribute.ManasCoreAttributes;
import com.github.manasmods.tensura.effect.template.SkillMobEffect;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

public class AugmentedFrameEffect extends SkillMobEffect {
    protected static final String AUGMENTED_FRAME = "55d0b895-74d9-4eeb-bdec-f6cba951c99e";

    public AugmentedFrameEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ARMOR, AUGMENTED_FRAME, (double)15.0F, Operation.ADDITION);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, AUGMENTED_FRAME, (double)15.0F, Operation.ADDITION);
        this.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, AUGMENTED_FRAME, (double)2.0F, Operation.ADDITION);
        this.addAttributeModifier(Attributes.JUMP_STRENGTH, AUGMENTED_FRAME, -0.2, Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, AUGMENTED_FRAME, 0.4, Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, AUGMENTED_FRAME, -0.05, Operation.ADDITION);
        this.addAttributeModifier((Attribute)ForgeMod.SWIM_SPEED.get(), AUGMENTED_FRAME, -0.05, Operation.ADDITION);
        this.addAttributeModifier((Attribute)ForgeMod.STEP_HEIGHT_ADDITION.get(), AUGMENTED_FRAME, (double)1.0F, Operation.ADDITION);
        this.addAttributeModifier((Attribute)ManasCoreAttributes.JUMP_POWER.get(), AUGMENTED_FRAME, -0.2, Operation.ADDITION);
        this.addAttributeModifier((Attribute)ForgeMod.ATTACK_RANGE.get(), AUGMENTED_FRAME, (double)1.0F, Operation.ADDITION);
        this.addAttributeModifier((Attribute)ForgeMod.REACH_DISTANCE.get(), AUGMENTED_FRAME, (double)1.0F, Operation.ADDITION);
        this.addAttributeModifier((Attribute)TensuraAttributeRegistry.SIZE.get(), AUGMENTED_FRAME, (double)0.5F, Operation.MULTIPLY_TOTAL);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            if (!pLivingEntity.getLevel().isClientSide()) {
                player.getFoodData().eat(pAmplifier + 1, 0.5F);
            }
        } else if (pLivingEntity.getHealth() < pLivingEntity.getMaxHealth()) {
            pLivingEntity.heal(1.0F + (float)pAmplifier);
        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

