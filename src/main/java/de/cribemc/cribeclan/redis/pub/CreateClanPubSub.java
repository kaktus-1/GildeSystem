package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.redis.RedisPubSub;

import java.util.UUID;

public class CreateClanPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clan = new Clan(args[0], args[1]);
        clan.getUsers().add(new User(UUID.fromString(args[2]),
                args[3],
                clan,
                Rank.OWNER));
        clanRegistry.getClans().add(clan);
    }
}
