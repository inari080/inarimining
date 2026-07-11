package com.inari.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InariMiningClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("inarimining");

    @Override
    public void onInitializeClient() {
        LOGGER.info("InariMiningClient initialized!");
    }
}