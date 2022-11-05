package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.redis.RedisPubSub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class AnnouncePubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        ArrayList<User> users = clanFromName.getUsers();
        List<UUID> notTo = args.length == 3 ? Arrays.stream(args[2].split(","))
                .map(UUID::fromString)
                .collect(Collectors.toList()) : Collections.emptyList();

        for (User user : users) {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null && !notTo.contains(player.getUniqueId()))
                player.sendMessage(args[1]);
        }
    }

}
