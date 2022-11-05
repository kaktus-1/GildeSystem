package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.redis.RedisPubSub;

public class UpdateNamePubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        clanFromName.setName(args[1]);
    }
}
