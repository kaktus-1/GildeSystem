package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class DeRankSubCommand extends SubCommand {
    public DeRankSubCommand() {
        super("derank", "<user>", "Degradiere einen Spieler vom Clan-Moderator zurück zum Clan-Member.");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler befindet sich nicht in deinen Clan!");
        addDefault("c", "§7Dieser Spieler ist kein Clan-Moderator!");
        addDefault("d", "§b%player% §7hat dich degradiert. Du bist nun kein §2Clan-Moderator §7mehr.");
        addDefault("e", "§b%player% §7ist nun kein §2Clan-Moderator §7mehr.");
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

        if (user1.getRank() == Rank.MEMBER || user1.getRank() == Rank.OWNER) {
            sendMessage(player, "c");
            return true;
        }

        clan.announce(getString("e", Replace.of("player", user1.getName())),
                user1.getUuid());
        clan.sendTo(getString("d", Replace.of("player", player.getName())), user1.getUuid());
        clan.updatePlayerRank(user1, Rank.MEMBER);
        return true;
    }
}
