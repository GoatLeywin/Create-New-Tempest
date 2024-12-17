package net.goatmorreti.create_new_tempest.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab CREATE_NEW_TEMPEST_TAB = new CreativeModeTab("create_new_tempest_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.PINK_SLIME_POOP.get());
        }
    };
}
