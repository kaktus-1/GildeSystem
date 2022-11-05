package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.commands.subcommands.SubCommand;
import lombok.NonNull;

public class RejectSubCommand extends SubCommand {
    public RejectSubCommand() {
        super("reject", "[clan]", "Eingeladener Spieler kann Einladung ablehnen");
        setRequiresClan(false);
    }
}
