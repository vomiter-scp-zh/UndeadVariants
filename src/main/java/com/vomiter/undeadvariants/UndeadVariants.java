package com.vomiter.undeadvariants;

import com.mojang.logging.LogUtils;
import com.vomiter.undeadvariants.client.ClientModEvents;
import com.vomiter.undeadvariants.common.ModEvents;
import com.vomiter.undeadvariants.common.entity.conversion.ConversionRules;
import com.vomiter.undeadvariants.common.event.EventHandler;
import com.vomiter.undeadvariants.common.event.UVMobHunterHook;
import com.vomiter.undeadvariants.common.registry.ModRegistries;
import com.vomiter.undeadvariants.data.UndeadDataGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

@Mod(UndeadVariants.MOD_ID)
public class UndeadVariants
{
    // Define mod mobCivicsId in a common place for everything to reference
    public static final String MOD_ID = "undeadvariants";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UndeadVariants(FMLJavaModLoadingContext context) {
        ConversionRules.init();
        EventHandler.init();
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(UndeadDataGenerator::generateData);
        ModRegistries.register(modBus);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModEvents.init(modBus);

        if(FMLLoader.getDist().isClient()){
            ClientModEvents.init(modBus);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            UVMobHunterHook.init();
        });
    }

}
