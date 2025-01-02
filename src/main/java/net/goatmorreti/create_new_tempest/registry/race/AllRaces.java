package net.goatmorreti.create_new_tempest.registry.race;

import com.github.manasmods.tensura.registry.race.TensuraRaces;
import net.goatmorreti.create_new_tempest.race.mechanical_scholar.MechanicalExpertRace;
import net.goatmorreti.create_new_tempest.race.mechanical_scholar.MechanicalNoviceRace;
import net.goatmorreti.create_new_tempest.race.mechanical_scholar.MechanicalScholarRace;
import net.goatmorreti.create_new_tempest.race.mechanical_titan.MechanicalColossusRace;
import net.goatmorreti.create_new_tempest.race.MechanicalConstructRace;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.goatmorreti.create_new_tempest.race.mechanical_titan.MechanicalGiantRace;
import net.goatmorreti.create_new_tempest.race.mechanical_titan.MechanicalTitanRace;
import net.goatmorreti.create_new_tempest.race.mechanical_warrior.MechanicalCombatantRace;
import net.goatmorreti.create_new_tempest.race.mechanical_warrior.MechanicalSoldierRace;
import net.goatmorreti.create_new_tempest.race.mechanical_warrior.MechanicalWarriorRace;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(
        modid = CreateNewTempest.MOD_ID,
        bus = Bus.MOD
)
public class AllRaces {

    public static final ResourceLocation MECHANICAL_CONSTRUCT = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_construct");
    public static final ResourceLocation MECHANICAL_COLOSSUS = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_colossus");
    public static final ResourceLocation MECHANICAL_TITAN = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_titan");
    public static final ResourceLocation MECHANICAL_WARRIOR = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_warrior");
    public static final ResourceLocation MECHANICAL_GIANT = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_giant");
    public static final ResourceLocation MECHANICAL_SOLDIER = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_soldier");
    public static final ResourceLocation MECHANICAL_COMBATANT = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_combatant");
    public static final ResourceLocation MECHANICAL_EXPERT = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_expert");
    public static final ResourceLocation MECHANICAL_NOVICE = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_novice");
    public static final ResourceLocation MECHANICAL_SCHOLAR = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_scholar");

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(TensuraRaces.RACE_REGISTRY.get().getRegistryKey(), helper -> {
            helper.register("mechanical_construct", new MechanicalConstructRace());
            helper.register("mechanical_colossus", new MechanicalColossusRace());
            helper.register("mechanical_titan", new MechanicalTitanRace());
            helper.register("mechanical_warrior", new MechanicalWarriorRace());
            helper.register("mechanical_giant", new MechanicalGiantRace());
            helper.register("mechanical_soldier", new MechanicalSoldierRace());
            helper.register("mechanical_combatant", new MechanicalCombatantRace());
            helper.register("mechanical_expert", new MechanicalExpertRace());
            helper.register("mechanical_novice", new MechanicalNoviceRace());
            helper.register("mechanical_scholar", new MechanicalScholarRace());
        });
    }
}
