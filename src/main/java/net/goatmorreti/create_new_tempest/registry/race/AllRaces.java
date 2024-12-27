package net.goatmorreti.create_new_tempest.registry.race;

import com.github.manasmods.tensura.registry.race.TensuraRaces;
import net.goatmorreti.create_new_tempest.race.sculk.MechanicalColossusRace;
import net.goatmorreti.create_new_tempest.CreateNewTempest;
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

    public static final ResourceLocation MECHANICAL_COLOSSUS = new ResourceLocation(CreateNewTempest.MOD_ID, "mechanical_colossus");

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(TensuraRaces.RACE_REGISTRY.get().getRegistryKey(), helper -> {
            helper.register("mechanical_colossus", new MechanicalColossusRace());
        });
    }
}
