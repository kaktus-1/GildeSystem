package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

public class AcceptSubCommand extends SubCommand {
    public AcceptSubCommand() {
        super("accept", "[gilde]", "Eingeladener Spieler kann Einladung annehmen");
        setRequiresClan(false);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
