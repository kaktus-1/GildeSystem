package de.chunkeater32.utils;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.commands.subcommands.SubCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class ConfirmHelper {

    private final SubCommand subCommand;
    private final String[] args;
    private final Clan clan;
    @Setter
    private boolean done;

}
