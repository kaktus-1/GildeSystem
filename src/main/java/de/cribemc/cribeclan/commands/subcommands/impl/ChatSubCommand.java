package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

public class ChatSubCommand extends SubCommand {
    public ChatSubCommand() {
        super("chat", "", "Wechsle in den Clan-Chat und wieder zurück in den globalen Chat");
        addDefault("format", "§8• %role% §8| %prefix%%player% §8• §7%message%");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        return false;
    }
}
