package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.redis.RedisPubSub;

import java.util.UUID;

public class SetCustomRankForUserPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        User user = clanFromName.getUser(UUID.fromString(args[1]));
        user.setCustomRank(args[2] == null ? null : clanFromName.getCustomRank(args[2]));
    }
}
