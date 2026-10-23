package com.colorref.strinova;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = StrinovaMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = StrinovaMod.MODID, value = Dist.CLIENT)
public class StrinovaModClient {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        StrinovaMod.LOGGER.info("Strinova client initialized");
    }
}
