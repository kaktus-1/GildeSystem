package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

public class LeaveSubCommand extends SubCommand {
    public LeaveSubCommand() {
        super("leave", "", "Verlasse deinen Clan");
        addDefault("a", "§7Dieser Befehl ist Clan-Moderatoren und Clan-Membern vorbehalten!");
        addDefault("b", "§7Erfolgreich den Clan verlassen.");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() == Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        clan.removePlayer(player.getUniqueId());
        sendMessage(player, "b");
        return true;
    }
}
