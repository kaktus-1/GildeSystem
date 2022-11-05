package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

public class InviteSubCommand extends SubCommand {
    public InviteSubCommand() {
        super("invite", "<user>", "Versende eine Gilden-Einladung an einen bestimmten Spieler");
        addDefault("a", "§7Dieser Befehl ist Gilden-Moderatoren und dem Gilden-Inhaber vorbehalten!");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (args.length != 1)
            return false;

        if (user.getRank() != Rank.MODERATOR && user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }


        return true;
    }
}
