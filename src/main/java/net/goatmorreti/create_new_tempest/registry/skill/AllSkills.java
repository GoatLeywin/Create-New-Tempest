package net.goatmorreti.create_new_tempest.registry.skill;


import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.goatmorreti.create_new_tempest.ability.skill.extra.engineers_goggle_skill;
import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * This file is responsible for registering all the skills with the official Tensura Mod.
 * It works by using a single DeferredRegister, which is based off of the ManasCore Skill Registry.
 * Any skill you create must go into that registry.
 *
 * All Skill types can go in here.
 */
public class AllSkills {

    // Here is that deferred register I was talking about. You don't need to change it at all
    public static DeferredRegister<ManasSkill> skillRegistry = DeferredRegister.create(SkillAPI.getSkillRegistryKey(), CreateNewTempest.MOD_ID);

    public static void register(IEventBus modEventBus) {
        skillRegistry.register(modEventBus);
    }

    // Here is where the skills are registered. To add another skill, simply duplicate the RegistryObject, and change it to match your skill defined in ../ability/skill/

    //   =================
    //   | Common Skills |
    //   =================

    //   ====================
    //   | Intrinsic Skills |
    //   ====================

    //   ================
    //   | Extra Skills |
    //   ================
    public static final RegistryObject<engineers_goggle_skill> EXAMPLE_EXTRA =
            skillRegistry.register("engineers_goggle_skill", engineers_goggle_skill::new);

    //   =================
    //   | Unique Skills |
    //   =================

    //   =====================
    //   | Resistance Skills |
    //   =====================


    //   ===================
    //   | Ultimate Skills |
    //   ===================
}
