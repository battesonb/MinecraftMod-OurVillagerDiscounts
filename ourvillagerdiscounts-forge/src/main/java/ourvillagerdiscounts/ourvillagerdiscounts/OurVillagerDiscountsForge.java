package ourvillagerdiscounts.ourvillagerdiscounts;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ourvillagerdiscounts.ourvillagerdiscounts.event.VillagerInteractEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("ourvillagerdiscounts-forge")
public class OurVillagerDiscountsForge {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public OurVillagerDiscountsForge() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event) {
        // Process InterModComms from other mods
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}

    /**
     * Listen to all player interact events and post a new {@link VillagerInteractEvent} when the target is a villager.
     */
    @SubscribeEvent
    public void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract e) {
        Entity target = e.getTarget();
        if (target instanceof VillagerEntity) {
            MinecraftForge.EVENT_BUS.post(new VillagerInteractEvent(e.getPlayer(), (VillagerEntity)target));
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
    }
}
