package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class RemoveRoleSubCommand extends SubCommand {
    public RemoveRoleSubCommand() {
        super("removerole", "<user>", "Entfernt dem Spieler seine Rolle");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Dieser Spieler ist nicht in deinem Clan!");
        addDefault("c", "§7Dieser Spieler hat keine Rolle");
        addDefault("d", "§7Du hast erfolgreich §b%player% §7seine Rolle entfernt.");
        addDefault("e", "§b%player% §7hat dir deine Rolle entfernt.");
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

        if (user1.getCustomRank() == null) {
            sendMessage(player, "c");
            return true;
        }

        clan.setCustomRankForUser(user1, null);
        sendMessage(player, "d", Replace.of("player", user1.getName()));
        clan.sendTo(getString("e", Replace.of("player", player.getName())),
                user1.getUuid());
        return true;
    }
}
