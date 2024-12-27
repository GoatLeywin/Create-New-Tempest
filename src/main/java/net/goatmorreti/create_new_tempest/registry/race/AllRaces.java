package net.goatmorreti.create_new_tempest.registry.race;

import com.github.manasmods.tensura.registry.race.TensuraRaces;
import net.goatmorreti.create_new_tempest.race.mechanical_construct.MechanicalColossusRace;
import net.goatmorreti.create_new_tempest.race.mechanical_construct.MechanicalConstructRace;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.goatmorreti.create_new_tempest.race.mechanical_construct.MechanicalTitanRace;
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

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(TensuraRaces.RACE_REGISTRY.get().getRegistryKey(), helper -> {
            helper.register("mechanical_construct", new MechanicalConstructRace());
            helper.register("mechanical_colossus", new MechanicalColossusRace());
            helper.register("mechanical_titan", new MechanicalTitanRace());
        });
    }
}
