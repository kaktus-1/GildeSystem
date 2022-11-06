package de.cribemc.cribeclan.commands.subcommands.impl;

import co.aikar.idb.DB;
import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.ChatSwitchPubSub;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

public class ChatSubCommand extends SubCommand {
    public ChatSubCommand() {
        super("chat", "", "Wechsle in den Gilden-Chat und wieder zurück in den globalen Chat");
        addDefault("format", "§8• %role% §8| %prefix%%player% §8• §7%message%");
        addDefault("a", "§7Du bist nun im Gilden Chat");
        addDefault("b", "§7Du bist nun nicht mehr im Gilden Chat");
    }

    @Override
    @SneakyThrows
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        RedisRegistry redisRegistry = getInstance().getRedisRegistry();

        String name = clan.getName();
        String string = user.getUuid().toString();

        if (user.isClanChat()) {
            DB.executeUpdate("UPDATE `users` SET `clanchat` = ? WHERE `uuid` = ?",
                    "false", user.getUuid().toString());
            redisRegistry.send(ChatSwitchPubSub.class, name, string, "false");
            sendMessage(player, "b");
            return true;
        }

        DB.executeUpdate("UPDATE `users` SET `clanchat` = ? WHERE `uuid` = ?",
                "true", user.getUuid().toString());
        redisRegistry.send(ChatSwitchPubSub.class, name, string, "true");
        sendMessage(player, "a");
        return true;
    }
}
