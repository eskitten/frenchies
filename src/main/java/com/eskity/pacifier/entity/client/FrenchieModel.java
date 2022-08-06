package com.eskity.pacifier.entity.client;

import com.eskity.pacifier.Pacifier;
import com.eskity.pacifier.entity.custom.FrenchieEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FrenchieModel extends AnimatedGeoModel<FrenchieEntity> {
    @Override
    public Identifier getModelResource(FrenchieEntity object) {
        return new Identifier(Pacifier.MODID,"geo/frenchie.geo.json");
    }

    @Override
    public Identifier getTextureResource(FrenchieEntity object) {
        return FrenchieRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }
    @Override
    public Identifier getAnimationResource(FrenchieEntity animatable) {
        return new Identifier(Pacifier.MODID, "animations/frenchie.animation.json");
    }
}
