package com.eskity.pacifier.entity;

import com.eskity.pacifier.Pacifier;
import com.eskity.pacifier.entity.custom.FrenchieEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<FrenchieEntity> FRENCHIE
            = Registry.register(Registry.ENTITY_TYPE, new Identifier(Pacifier.MODID, "frenchie"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FrenchieEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.75F)).build());
}
