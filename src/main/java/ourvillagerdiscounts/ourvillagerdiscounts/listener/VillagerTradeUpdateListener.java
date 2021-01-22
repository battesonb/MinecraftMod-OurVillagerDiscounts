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

public class VillagerTradeUpdateListener implements VillagerInteractCallback {

    private static final Logger log = LogManager.getLogger(VillagerTradeUpdateListener.class);

    @Override
    public ActionResult interact(PlayerEntity player, VillagerEntity villager) {
        final VillagerData data = villager.getVillagerData();
        final VillagerProfession profession = data.getProfession();

        if (!profession.equals(VillagerProfession.NONE) && !profession.equals(VillagerProfession.NITWIT)) {
            final VillagerGossips gossip = villager.getGossip();

            try {
                final Tag gossipNbt = gossip.serialize(NbtOps.INSTANCE).getValue();

                // the rest of this code is gross, but likely cheaper than serialization, anyway.
                final String gossipNbtString = gossipNbt.toString();
                int index = gossipNbtString.indexOf("major_positive") + 15;
                final StringBuilder sb = new StringBuilder();
                while (index < gossipNbtString.length()) {
                    final char curr = gossipNbtString.charAt(++index);
                    if (curr >= 48 && curr <= 57) {
                        sb.append(curr);
                    }
                    if (curr == '}') {
                        break;
                    }
                }

                final int majorPositiveGossip = Integer.parseInt(sb.toString());
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
            } catch (Exception e) {
                log.warn(e);
            }
        }

        return ActionResult.PASS;
    }
}
