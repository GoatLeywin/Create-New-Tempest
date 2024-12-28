package net.goatmorreti.create_new_tempest.registry.effect;

import net.goatmorreti.create_new_tempest.effect.OverclockEffect;
import java.awt.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllEffects {
    private static final DeferredRegister<MobEffect> registry;
    public static final RegistryObject<MobEffect> OVERCLOCK;

    public AllEffects() {
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "create_new_tempest");
        OVERCLOCK = registry.register("overclock", () -> new OverclockEffect(MobEffectCategory.BENEFICIAL, (new Color(63, 65, 65)).getRGB()));
    }
}
