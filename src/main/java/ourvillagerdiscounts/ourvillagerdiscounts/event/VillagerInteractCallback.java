package ourvillagerdiscounts.ourvillagerdiscounts.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/**
 * Callback for interacting with a villager.
 * Upon return:
 * - SUCCESS cancels further processing and continues with normal interaction behavior.
 * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available.
 * - FAIL cancels further processing and does not interact with the villager.
 */
public interface VillagerInteractCallback {
    Event<VillagerInteractCallback> EVENT = EventFactory.createArrayBacked(VillagerInteractCallback.class,
            (listeners) -> (player, villager) -> {
                for (VillagerInteractCallback listener : listeners) {
                    ActionResult result = listener.interact(player, villager);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, VillagerEntity villager);
}
