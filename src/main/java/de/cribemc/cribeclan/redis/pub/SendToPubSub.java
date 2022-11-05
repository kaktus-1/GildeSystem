package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.redis.RedisPubSub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SendToPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Player player = Bukkit.getPlayer(UUID.fromString(args[0]));
        if (player != null)
            player.sendMessage(args[1]);
    }
}
