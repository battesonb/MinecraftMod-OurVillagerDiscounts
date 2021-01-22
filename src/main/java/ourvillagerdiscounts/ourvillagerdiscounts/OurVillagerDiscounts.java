package ourvillagerdiscounts.ourvillagerdiscounts;

import net.fabricmc.api.ModInitializer;
import ourvillagerdiscounts.ourvillagerdiscounts.event.VillagerInteractCallback;
import ourvillagerdiscounts.ourvillagerdiscounts.listener.VillagerTradeUpdateListener;

public class OurVillagerDiscounts implements ModInitializer {
    @Override
    public void onInitialize() {
        VillagerInteractCallback.EVENT.register(new VillagerTradeUpdateListener());
    }
}
