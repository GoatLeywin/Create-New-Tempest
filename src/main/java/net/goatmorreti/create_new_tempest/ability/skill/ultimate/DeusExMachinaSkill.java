package net.goatmorreti.create_new_tempest.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.ability.skill.Skill.SkillType;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.github.manasmods.tensura.ability.skill.extra.DemonLordHakiSkill;
import com.github.manasmods.tensura.ability.skill.extra.MultilayerBarrierSkill;
import com.github.manasmods.tensura.ability.skill.unique.ReverserSkill;

public class DeusExMachinaSkill extends Skill {
    private static final UUID MULTILAYER = UUID.fromString("2c03b682-5705-11ee-8c99-0242ac120002");
    private static final UUID ACCELERATION = UUID.fromString("46dc5eee-34e9-4a6c-ad3d-58048cb06c6f");

    public DeusExMachinaSkill() {
        super(SkillType.UNIQUE);
    }

    public double learningCost() {
        return 10000.0;
    }

    public int modes() {
        return 4;
    }

    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        int mode = instance.getMode();
        return reverse ? (mode == 1 ? 4 : mode - 1) : (mode == 4 ? 1 : mode + 1);
    }

    public Component getModeName(int mode) {
        return switch (mode) {
            case 1 -> Component.literal("Universal Perception");
            case 2 -> Component.literal("Demon Lord's Haki");
            case 3 -> Component.literal("Inverted Fusion");
            case 4 -> Component.literal("Multilayer Barrier");
            default -> Component.empty();
        };
    }

    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {

    }

    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {

    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()) {
            case 1 -> universalPerception(instance, entity);
            case 2 -> demonLordsHaki(instance, entity);
            case 3 -> invertedFusion(instance, entity);
            case 4 -> multilayerBarrier(instance, entity);
        }
    }

    private void universalPerception(ManasSkillInstance instance, LivingEntity entity) {
        // Placeholder for Universal Perception logic
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private void demonLordsHaki(ManasSkillInstance instance, LivingEntity entity) {
        DemonLordHakiSkill hakiSkill = new DemonLordHakiSkill();
        //TensuraSkillInstance hakiInstance = new TensuraSkillInstance(hakiSkill);

        // Synchronize modes or set to Mode 2 directly
        //hakiInstance.setMode(2); // Set mode to "Coat"
        //hakiSkill.onPressed(hakiInstance, entity);
        hakiSkill.onPressed(instance, entity);
    }

    private void invertedFusion(ManasSkillInstance instance, LivingEntity entity) {
        ReverserSkill reverserSkill = new ReverserSkill();
        reverserSkill.onPressed(instance, entity);
    }

    private void multilayerBarrier(ManasSkillInstance instance, LivingEntity entity) {
        MultilayerBarrierSkill barrierSkill = new MultilayerBarrierSkill();
        barrierSkill.onPressed(instance, entity);
    }
}
