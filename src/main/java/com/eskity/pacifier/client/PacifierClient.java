package com.eskity.pacifier.client;

import com.eskity.pacifier.Pacifier;
import com.eskity.pacifier.entity.ModEntities;
import com.eskity.pacifier.entity.client.FrenchieRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class PacifierClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Pacifier.LOGGER.debug("Initializing Mods for " + Pacifier.MODID);
        EntityRendererRegistry.register(ModEntities.FRENCHIE, FrenchieRenderer::new);
    }

}
