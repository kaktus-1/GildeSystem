package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.Rank;
import de.chunkeater32.clan.User;
import de.chunkeater32.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

public class InviteSubCommand extends SubCommand {
    public InviteSubCommand() {
        super("invite", "<user>", "Versende eine Clan-Einladung an einen bestimmten Spieler");
        addDefault("a", "§7Dieser Befehl ist Clan-Moderatoren und dem Clan-Inhaber vorbehalten!");
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
