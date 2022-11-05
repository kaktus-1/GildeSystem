package de.cribemc.cribeclan.clan;

import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.CreateClanPubSub;
import de.cribemc.cribeclan.redis.pub.DeleteClanPubSub;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class ClanRegistry {

    private final RedisRegistry redisRegistry = CribeClan.getInstance().getRedisRegistry();
    private final ArrayList<Clan> clans = new ArrayList<>();

    public ClanRegistry() {

    }

    public void createClan(Player owner, String name, String tag) {
        redisRegistry.send(CreateClanPubSub.class, name,
                tag,
                owner.getUniqueId().toString(),
                owner.getName());
    }

    public void deleteClan(Clan clan) {
        redisRegistry.send(DeleteClanPubSub.class, clan.getName());
    }

    public Clan getClanFromUser(Player player) {
        return getClanFromUser(player.getUniqueId());
    }

    public Clan getClanFromUser(UUID user) {
        return clans.stream()
                .filter(clan -> clan.getUser(user) != null)
                .findFirst().orElse(null);
    }

    public Clan getClanFromName(String name) {
        return clans.stream()
                .filter(clan -> clan.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public Clan getClanFromTag(String tag) {
        return clans.stream()
                .filter(clan -> clan.getTag().equalsIgnoreCase(tag))
                .findFirst().orElse(null);
    }

    public User getUser(Player player) {
        Clan clanFromUser = getClanFromUser(player);

        if (clanFromUser == null)
            return null;

        return clanFromUser.getUser(player);
    }
}
