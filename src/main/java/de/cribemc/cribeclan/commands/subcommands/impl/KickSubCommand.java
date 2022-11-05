package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class KickSubCommand extends SubCommand {
    public KickSubCommand() {
        super("kick", "<user>", "Wirft einen bestimmten Spieler aus dem Clan");
        addDefault("a", "§7Dieser Befehl ist Clan-Moderatoren und dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler ist nicht in deinem Clan!");
        addDefault("c", "§b%player% §7wurde aus dem Clan geworfen.");
        addDefault("d", "§7Du wurdest aus dem Clan geworfen!");
        addDefault("e", "§b%player% §7wurde aus dem Clan geworfen.");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() == Rank.MEMBER) {
            sendMessage(player, "a");
            return true;
        }

        if (args.length != 1)
            return false;

        User user1 = clan.getUser(args[0]);

        if (user1 == null) {
            sendMessage(player, "b");
            return true;
        }

        clan.sendTo(getString("d"), user1.getUuid());
        clan.removePlayer(user1.getUuid());
        sendMessage(player, "c", Replace.of("player", user1.getName()));
        clan.announce(getString("e", Replace.of("player", user1.getName())), player.getUniqueId());
        return true;
    }
}
