package com.talhanation.siegeweapons;
import com.talhanation.siegeweapons.client.events.KeyEvents;
import com.talhanation.siegeweapons.client.events.ClientRenderEvents;
import com.talhanation.siegeweapons.client.gui.SiegeTableScreen;
import com.talhanation.siegeweapons.config.SiegeWeaponsServerConfig;
import com.talhanation.siegeweapons.init.ModBlocks;
import com.talhanation.siegeweapons.init.ModEntityTypes;
import com.talhanation.siegeweapons.init.ModItems;
import com.talhanation.siegeweapons.init.ModMenus;
import com.talhanation.siegeweapons.init.*;
import com.talhanation.siegeweapons.network.*;
import de.maxhenkel.corelib.CommonRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "siegeweapons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public Main() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SiegeWeaponsServerConfig.SERVER);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RecruitsClientConfig.CLIENT);
        //RecruitsClientConfig.loadConfig(RecruitsClientConfig.CLIENT, FMLPaths.CONFIGDIR.get().resolve("recruits-client.toml"));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    FMLJavaModLoadingContext.get().getModEventBus().addListener(Main.this::clientSetup);
                    FMLJavaModLoadingContext.get().getModEventBus().addListener(ModShortcuts::registerBindings);
                }
        );

        modEventBus.addListener(this::setup);
        ModSounds.SOUNDS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModPois.POIS.register(modEventBus);
        //ModProfessions.PROFESSIONS.register(modEventBus);
        ModMenus.MENU_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCreativeTabs);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ServerSideEvents());

        MinecraftForge.EVENT_BUS.register(new UpdateChecker());
        MinecraftForge.EVENT_BUS.register(this);

        Class[] messages = {
                MessageUpdateVehicleControl.class,
                MessageLoadAndShootWeapon.class,
                MessageUpdateSiegeTable.class,
                MessageSetCatapultRange.class,
                MessageOpenGUI.class,
                MessageTryLoadFromHand.class
        };

        for (int i = 0; i < messages.length; i++){
            CommonRegistry.registerMessage(SIMPLE_CHANNEL, i, messages[i]);
        }

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ModMenus::registerMenus);
        MinecraftForge.EVENT_BUS.register(new KeyEvents());
        MinecraftForge.EVENT_BUS.register(new ClientRenderEvents());
        MenuScreens.register(ModMenus.SIEGE_TABLE_CONTAINER.get(), SiegeTableScreen::new);
    }

    private void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            //event.accept(ModItems.BOWMAN_SPAWN_EGG.get());
        }

        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)){
            event.accept(ModBlocks.SIEGE_TABLE_BLOCK.get());
        }
    }
}
