package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.redis.RedisPubSub;

import java.util.UUID;

public class AddPlayerPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        User user = new User(UUID.fromString(args[1]), args[2], clanFromName, Rank.valueOf(args[3]));
        clanFromName.getUsers().add(user);
    }
}
