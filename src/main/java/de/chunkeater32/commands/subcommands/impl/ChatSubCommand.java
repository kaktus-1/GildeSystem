package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.User;
import de.chunkeater32.commands.subcommands.SubCommand;
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
