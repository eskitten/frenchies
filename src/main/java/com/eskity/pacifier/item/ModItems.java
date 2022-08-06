package com.eskity.pacifier.item;
import com.eskity.pacifier.Pacifier;
import com.eskity.pacifier.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item ROSE_GEM
            = registerItem("rose_gem", new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item ROSE_GEM_BLOCK
            = registerItem("rose_gem_block", new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item FRENCHIE_SPAWN_EGG
            = registerItem("frenchie_spawn_egg", new SpawnEggItem(ModEntities.FRENCHIE, 0x35335, 0x222222,
            new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Pacifier.MODID, name), item);
    }
    public static void registerModItems() {
        Pacifier.LOGGER.debug("Registering Mod Items for " + Pacifier.MODID);
    }
}
