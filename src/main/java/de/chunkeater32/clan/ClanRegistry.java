package de.chunkeater32.clan;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class ClanRegistry {

    private final ArrayList<Clan> clans = new ArrayList<>();

    public ClanRegistry() {

    }

    public void createClan(Player owner, String name, String tag) {
        Clan clan = new Clan(name, tag);
        clan.addPlayer(owner, Rank.OWNER);
        clans.add(clan);
    }

    public void deleteClan(Clan clan) {
        clans.remove(clan);
    }

    public Clan getClanFromUser(Player player) {
        return getClanFromUser(player.getUniqueId());
    }

    public Clan getClanFromUser(UUID user) {
        return clans.stream()
                .filter(clan -> clan.getUser(user) != null)
                .findFirst().orElse(null);
    }

    public Clan getClanFromUser(String user) {
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
