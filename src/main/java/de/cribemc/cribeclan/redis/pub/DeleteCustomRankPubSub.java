package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.CustomRank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.redis.RedisPubSub;

public class DeleteCustomRankPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        CustomRank customRank = clanFromName.getCustomRank(args[1]);
        for (User user : clanFromName.getUsers()) {
            if (user.getCustomRank() == customRank)
                user.setCustomRank(null);
        }
        clanFromName.getCustomRanks().remove(customRank);
    }
}
