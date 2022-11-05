package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.CustomRank;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class AddRoleSubCommand extends SubCommand {
    public AddRoleSubCommand() {
        super("addrole", "<user> <role>", "Weist einem Spieler eine bestimmte Clan-Rolle zu (maximal 1)");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler ist nicht in deinem Clan!");
        addDefault("c", "§7Diese Rolle existiert nicht.");
        addDefault("d", "§b%player% §7wurde die Rolle %role% §7zugewiesen!");
        addDefault("e", "§b%player% §7hat dir die Rolle %role% §7zugewiesen!");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        if (args.length != 2)
            return false;

        User user1 = clan.getUser(args[0]);

        if (user1 == null) {
            sendMessage(player, "b");
            return true;
        }

        CustomRank customRank = clan.getCustomRank(args[1]);
        if (customRank == null) {
            sendMessage(player, "c");
            return true;
        }

        clan.setCustomRankForUser(user1, customRank);
        sendMessage(player, "d", Replace.of("player", user1.getName()),
                Replace.of("role", customRank.getDisplayName()));
        clan.sendTo(getString("e", Replace.of("player", player.getName()),
                        Replace.of("role", customRank.getDisplayName())),
                user1.getUuid());
        return true;
    }
}
