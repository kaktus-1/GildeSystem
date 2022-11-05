package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.commands.subcommands.SubCommand;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class AcceptSubCommand extends SubCommand {
    public AcceptSubCommand() {
        super("accept", "[clan]", "Eingeladener Spieler kann Einladung annehmen");
        setRequiresClan(false);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
