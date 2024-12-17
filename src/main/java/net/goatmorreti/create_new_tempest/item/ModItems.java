package net.goatmorreti.create_new_tempest.item;

import net.goatmorreti.create_new_tempest.CreateNewTempest;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateNewTempest.MOD_ID);

    public static  final RegistryObject<Item> PINK_SLIME_POOP = ITEMS.register("pink_slime_poop",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CREATE_NEW_TEMPEST_TAB)));
    public static  final RegistryObject<Item> RED_SLIME_POOP = ITEMS.register("red_slime_poop",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CREATE_NEW_TEMPEST_TAB)));
    public static  final RegistryObject<Item> TEAL_SLIME_POOP = ITEMS.register("teal_slime_poop",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CREATE_NEW_TEMPEST_TAB)));
    public static  final RegistryObject<Item> BLUE_SLIME_POOP = ITEMS.register("blue_slime_poop",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CREATE_NEW_TEMPEST_TAB)));
    public static  final RegistryObject<Item> YELLOW_SLIME_POOP = ITEMS.register("yellow_slime_poop",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CREATE_NEW_TEMPEST_TAB)));
    public static  final RegistryObject<Item> GREEN_SLIME_POOP = ITEMS.register("green_slime_poop",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CREATE_NEW_TEMPEST_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
