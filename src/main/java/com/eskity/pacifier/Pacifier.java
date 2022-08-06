package com.eskity.pacifier;

import com.eskity.pacifier.item.ModItems;
import com.eskity.pacifier.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class Pacifier implements ModInitializer {
    public static final String MODID = "pacifier";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        ModRegistries.registerModStuffs();
        ModItems.registerModItems();
        GeckoLib.initialize();
    }
}
