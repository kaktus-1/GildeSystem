package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.AcceptPubSub;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

import java.util.Map;

public class AcceptSubCommand extends SubCommand {
    public AcceptSubCommand() {
        super("accept", "<gilde>", "Eingeladener Spieler kann Einladung annehmen");
        setRequiresClan(false);
        addDefault("a", "§7Du wurdest zu keiner Gilde eingeladen.");
        addDefault("b", "§b%player%§7 ist der Gilde beigetreten.");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 1)
            return false;

        Map.Entry<Clan, String> entry = InviteSubCommand.invited.entrySet()
                .stream()
                .filter(clanStringEntry -> clanStringEntry.getKey().getName().equalsIgnoreCase(args[0]))
                .findFirst().orElse(null);

        if (entry == null) {
            sendMessage(player, "a");
            return true;
        }

        Clan key = entry.getKey();

        RedisRegistry redisRegistry = getInstance().getRedisRegistry();
        redisRegistry.send(AcceptPubSub.class, key.getName());
        key.addPlayer(player);
        key.announce(getString("b", Replace.of("player", player.getName())));
        return true;
    }
}
