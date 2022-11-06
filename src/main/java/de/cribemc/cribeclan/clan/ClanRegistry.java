package de.cribemc.cribeclan.clan;

import co.aikar.idb.DB;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.CreateClanPubSub;
import de.cribemc.cribeclan.redis.pub.DeleteClanPubSub;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class ClanRegistry {

    private final ArrayList<Clan> clans = new ArrayList<>();

    private RedisRegistry getRedisRegistry() {
        return CribeClan.getInstance().getRedisRegistry();
    }

    @SneakyThrows
    public void createClan(Player owner, String name, String tag) {
        DB.executeInsert("INSERT INTO `clans` (`name`, `tag`) VALUES (?, ?)",
                name, tag);
        DB.executeInsert("INSERT INTO `users` (`uuid`, `name`, `clan`, `rank`, `customrank`, `clanchat`) VALUES (?, ?, ?, ?, ?, ?)",
                owner.getUniqueId().toString(), owner.getName(), name, Rank.OWNER.name(), null, "false");
        getRedisRegistry().send(CreateClanPubSub.class, name,
                tag,
                owner.getUniqueId().toString(),
                owner.getName());
    }

    @SneakyThrows
    public void deleteClan(Clan clan) {
        String name = clan.getName();

        DB.executeUpdate("DELETE FROM `clans` WHERE `name` = ?", name);
        DB.executeUpdate("DELETE FROM `users` WHERE `clan` = ?", name);
        DB.executeUpdate("DELETE FROM `customranks` WHERE `clan` = ?", name);
        getRedisRegistry().send(DeleteClanPubSub.class, name);
    }

    public Clan getClanFromUser(Player player) {
        return getClanFromUser(player.getUniqueId());
    }

    public Clan getClanFromUser(String player) {
        return clans.stream()
                .filter(clan -> clan.getUser(player) != null)
                .findFirst().orElse(null);
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
