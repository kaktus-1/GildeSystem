package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class UpRankSubCommand extends SubCommand {
    public UpRankSubCommand() {
        super("uprank", "<user>", "Befördere einen Spieler vom Clan-Member zum Clan-Moderator");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler befindet sich nicht in deinen Clan!");
        addDefault("c", "§7Dieser Spieler ist bereits Clan-Moderator!");
        addDefault("d", "§b%player% §7hat dich zum §2Clan-Moderator §7befördert.");
        addDefault("e", "§b%player% §7ist nun §2Clan-Moderator§7.");
        addDefault("f", "§b%player% §7ist nun §2Clan-Moderator §7in unserem Clan.");
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

        if (user1.getRank() == Rank.MODERATOR || user1.getRank() == Rank.OWNER) {
            sendMessage(player, "c");
            return true;
        }

        sendMessage(player, "e", Replace.of("player", user1.getName()));
        clan.announce(getString("f", Replace.of("player", user1.getName())),
                player.getUniqueId(),
                user1.getUuid());
        clan.sendTo(getString("d", Replace.of("player", player.getName())), user1.getUuid());
        clan.updatePlayerRank(user1, Rank.MODERATOR);
        return true;
    }
}
