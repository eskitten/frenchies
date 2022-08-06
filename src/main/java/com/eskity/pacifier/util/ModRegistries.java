package com.eskity.pacifier.util;

import com.eskity.pacifier.entity.ModEntities;
import com.eskity.pacifier.entity.custom.FrenchieEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModRegistries {
    public static void registerModStuffs() {
        registerAttributes();
    }
    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.FRENCHIE, FrenchieEntity.setAttributes());
    }
}
