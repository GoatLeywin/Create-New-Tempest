package net.goatmorreti.create_new_tempest.ability.skill.intrinsic;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;

/**
 * Mechanical Hands skill for using wrench-like functionality on press.
 */
public class MechanicalHandsSkill extends Skill {

    private final double skillCastCost = 20.0;  // Magicule cost per activation
    private final int numModes = 2; // Two modes: Rotate and Remove

    public MechanicalHandsSkill() {
        super(SkillType.INTRINSIC);
    }

    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(CreateNewTempest.MOD_ID, "textures/skill/intrinsic/mechanical_hands.png");
    }

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        double cost;
        switch (instance.getMode()) {
            case 1 -> cost = 1.0;
            case 2 -> cost = 2.0;
            default -> cost = 0.0;
        }

        return cost;
    }

    public int modes() {
        return numModes;  // Two modes: rotate (1) and remove (2)
    }

    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? numModes : instance.getMode() - 1;
        } else {
            return instance.getMode() == numModes ? 1 : instance.getMode() + 1;
        }
    }

    public Component getModeName(int curMode) {
        MutableComponent name;
        switch (curMode) {
            case 1 -> name = Component.translatable("create_new_tempest.skill.mode.mechanical_hands.rotate");
            case 2 -> name = Component.translatable("create_new_tempest.skill.mode.mechanical_hands.remove");
            default -> name = Component.empty();
        }

        return name;
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {

        // Check if the player has enough magicules
        if (SkillHelper.outOfMagicule(entity, skillCastCost)) {
            return;
        }

        // Handle logic based on the current mode
        switch (instance.getMode()) {
            case 1: // Mode 1: Rotate functionality (current "rotate" behavior)
                rotateBlock((Player) entity, instance);
                break;
            case 2: // Mode 2: Remove functionality (pick up block using onSneakWrenched)
                removeBlock((Player) entity, instance);
                break;
            default:
                break;
        }
    }

    private void rotateBlock(Player player, ManasSkillInstance instance) {
        // Get the block the player is looking at
        HitResult hitResult = player.pick(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos pos = blockHitResult.getBlockPos();
            Level world = player.getLevel();
            BlockState state = world.getBlockState(pos);

            // Check if the block is wrenchable and perform rotation
            if (state.getBlock() instanceof IWrenchable wrenchable) {
                InteractionResult result = wrenchable.onWrenched(state, new UseOnContext(world, player, InteractionHand.MAIN_HAND, player.getMainHandItem(), blockHitResult));
                if (result == InteractionResult.SUCCESS) {
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), AllSoundEvents.WRENCH_ROTATE.getMainEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);
                } else {
                    player.displayClientMessage(Component.translatable("Mechanical Hands: No valid target or action!"), true);
                }
            } else {
                player.displayClientMessage(Component.translatable("Mechanical Hands: This block is not wrenchable."), true);
            }
        }
    }

    private void removeBlock(Player player, ManasSkillInstance instance) {
        // Get the block the player is looking at
        HitResult hitResult = player.pick(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos pos = blockHitResult.getBlockPos();
            Level world = player.getLevel();
            BlockState state = world.getBlockState(pos);

            // Check if the block is wrenchable and perform removal
            if (state.getBlock() instanceof IWrenchable wrenchable) {
                InteractionResult result = wrenchable.onSneakWrenched(state, new UseOnContext(world, player, InteractionHand.MAIN_HAND, player.getMainHandItem(), blockHitResult));
                if (result == InteractionResult.SUCCESS) {
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), AllSoundEvents.WRENCH_REMOVE.getMainEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);
                } else {
                    player.displayClientMessage(Component.translatable("Mechanical Hands: No valid target or action!"), true);
                }
            } else {
                player.displayClientMessage(Component.translatable("Mechanical Hands: This block is not wrenchable."), true);
            }
        }
    }
}

