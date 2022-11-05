package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.CustomRank;
import de.cribemc.cribeclan.redis.RedisPubSub;

public class CreateCustomRankPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        clanFromName.getCustomRanks().add(new CustomRank(args[1], args[2]));
    }
}
