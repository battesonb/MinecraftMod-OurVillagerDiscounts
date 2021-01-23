package ourvillagerdiscounts.ourvillagerdiscounts.listener;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerGossips;
import net.minecraft.village.VillagerProfession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ourvillagerdiscounts.ourvillagerdiscounts.event.VillagerInteractCallback;
import ourvillagerdiscounts.ourvillagerdiscounts.mixin.VillagerGossipEntriesInvoker;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class VillagerTradeUpdateListener implements VillagerInteractCallback {

    private static final Logger log = LogManager.getLogger(VillagerTradeUpdateListener.class);

    @Override
    public ActionResult interact(PlayerEntity player, VillagerEntity villager) {
        final VillagerData data = villager.getVillagerData();
        final VillagerProfession profession = data.getProfession();

        if (!profession.equals(VillagerProfession.NONE) && !profession.equals(VillagerProfession.NITWIT)) {
            final VillagerGossips gossip = villager.getGossip();
            final Stream<VillagerGossips.GossipEntry> gossipAccessor = ((VillagerGossipEntriesInvoker)gossip).invokeEntries();
            gossipAccessor
                    .filter(a -> a.type.equals(VillageGossipType.MAJOR_POSITIVE))
                    .max(Comparator.comparingInt(a -> a.value))
                    .ifPresent(maxEntry -> {
                        final int majorPositiveGossip = maxEntry.value;
                        final int currentMajorPositiveGossip = gossip.getReputationFor(player.getUuid(), g -> g.equals(VillageGossipType.MAJOR_POSITIVE));

                        if (majorPositiveGossip > currentMajorPositiveGossip) {
                            final ListTag list = new ListTag();
                            final CompoundTag tag = new CompoundTag();
                            tag.putString("Type", "major_positive");
                            tag.putInt("Value", majorPositiveGossip);
                            tag.putUuid("Target", player.getUuid());
                            list.add(tag);
                            villager.setGossipDataFromTag(list);
                        }
                    });
        }

        return ActionResult.PASS;
    }
}
