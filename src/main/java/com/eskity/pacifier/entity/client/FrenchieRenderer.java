package com.eskity.pacifier.entity.client;

import com.eskity.pacifier.Pacifier;
import com.eskity.pacifier.entity.custom.FrenchieEntity;
import com.eskity.pacifier.entity.variant.FrenchieVariant;
import com.google.common.collect.Maps;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class FrenchieRenderer extends GeoEntityRenderer<FrenchieEntity> {
    public static final Map<FrenchieVariant, Identifier> LOCATION_BY_VARIANT
            = Util.make(Maps.newEnumMap(FrenchieVariant.class), (map) -> {
        map.put(FrenchieVariant.DEFAULT, new Identifier(Pacifier.MODID, "textures/entity/frenchie/frenchie.png"));
        map.put(FrenchieVariant.LIGHTGRAY, new Identifier(Pacifier.MODID, "textures/entity/frenchie/frenchie_gray.png"));
    });

    public FrenchieRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FrenchieModel());
    }


    @Override
    public Identifier getTextureResource(FrenchieEntity object) {
        return LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public RenderLayer getRenderType(FrenchieEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer,
                                     VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        if (animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        } else {
            stack.scale(1f, 1f, 1f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
