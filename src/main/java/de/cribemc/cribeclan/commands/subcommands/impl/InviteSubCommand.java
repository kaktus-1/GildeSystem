package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.InvitePubSub;
import de.cribemc.cribeclan.redis.pub.InviteTimeUpPubSub;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class InviteSubCommand extends SubCommand {

    public final static HashMap<Clan, String> invited = new HashMap<>();

    public InviteSubCommand() {
        super("invite", "<user>", "Versende eine Gilden-Einladung an einen bestimmten Spieler");
        addDefault("a", "§7Dieser Befehl ist Gilden-Moderatoren und dem Gilden-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler ist bereits Mitglied einer Gilde.");
        addDefault("c", "§7Bitte warte einen Moment bevor du noch jemanden einlädst.");
        addDefault("d", "§7Du hast §b%player% §7erfolgreich zur Gilde eingeladen!");
        addDefault("e", "§7Du wurdest von %player% zur Gilde %clan% eingeladen.");
        addDefault("f", "§b%player% §7hat die Gilden Einladung nicht angenommen.");
        addDefault("g", "§7Zeit zum Annehmen der Gilden Einladung für %clan% abgelaufen.");
        addDefault("h", "§7Dieser Spieler wurde bereits von jemanden eingeladen.\n§7Du musst warten bis diese Einladung abgelaufen ist.");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (args.length != 1)
            return false;

        if (user.getRank() != Rank.MODERATOR && user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        Clan clanFromUser = getClanRegistry().getClanFromUser(args[0]);

        if (clanFromUser != null) {
            sendMessage(player, "b");
            return true;
        }

        if (invited.containsKey(clan)) {
            sendMessage(player, "c");
            return true;
        }

        if (invited.containsValue(args[0])) {
            sendMessage(player, "h");
            return true;
        }

        sendMessage(player, "d", Replace.of("player", args[0]));
        RedisRegistry redisRegistry = getInstance().getRedisRegistry();
        redisRegistry.send(InvitePubSub.class, clan.getName(), args[0], player.getName());

        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
            if (invited.containsKey(clan) && invited.get(clan).equals(args[0]))
                redisRegistry.send(InviteTimeUpPubSub.class, player.getName(), args[0], clan.getName());
        }, 20 * 30);
        return true;
    }
}
