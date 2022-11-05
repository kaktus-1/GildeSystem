package de.cribemc.cribeclan.utils;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ConfirmHelper {

    private final SubCommand subCommand;
    private final String[] args;
    private final Clan clan;
    @Setter
    private boolean done;

}
