package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.commands.subcommands.SubCommand;

public class RejectSubCommand extends SubCommand {
    public RejectSubCommand() {
        super("reject", "[clan]", "Eingeladener Spieler kann Einladung ablehnen");
        setRequiresClan(false);
    }
}
