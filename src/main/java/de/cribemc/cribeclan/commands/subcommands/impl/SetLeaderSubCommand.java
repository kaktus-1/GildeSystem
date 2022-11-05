package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class SetLeaderSubCommand extends SubCommand {
    public SetLeaderSubCommand() {
        super("setleader", "<user>", "Übertragung der Eigentumsrechte an einen anderen Spieler");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler befindet sich nicht in deinen Clan!");
        addDefault("c", "§7Der Spieler muss Clan-Moderator sein.");
        addDefault("d", "§b%player% §7 ist nun Clan-Inhaber");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() != Rank.OWNER) {
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

        if (user1.getRank() != Rank.MODERATOR) {
            sendMessage(player, "c");
            return true;
        }

        clan.announce(getString("d", Replace.of("player", user1.getName())));
        clan.updatePlayerRank(user, Rank.MODERATOR);
        clan.updatePlayerRank(user1, Rank.OWNER);
        return true;
    }
}
