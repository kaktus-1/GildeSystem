package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.Rank;
import de.chunkeater32.clan.User;
import de.chunkeater32.commands.subcommands.SubCommand;
import de.chunkeater32.utils.ChatUtils;
import de.chunkeater32.utils.Replace;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class CreateRoleSubCommand extends SubCommand {
    public CreateRoleSubCommand() {
        super("createrole", "<role> <displayname>", "Erstellt eine neue Clan-Rolle");
        addDefault("regex", "(?i)[^A-Z0-9ÄÖÜ§]");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Es existiert bereits eine Rolle mit diesem Namen.");
        addDefault("c", "§7Du hast die Rolle §b%role% §7(§b%display%§7) erfolgreich erstellt.");
        addDefault("d", "§7Farbcodes dürfen nur in den Displaynamen!");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        if (args.length != 2)
            return false;

        if (clan.getCustomRank(args[0]) != null) {
            sendMessage(player, "b");
            return true;
        }

        if (args[0].contains("&")) {
            sendMessage(player, "d");
            return true;
        }

        args[1] = args[1].replace("&", "§");

        Pattern regex = Pattern.compile(getString("regex"));

        if (regex.matcher(args[0]).find() || regex.matcher(args[1]).find()) {
            ChatUtils.sendMessage(player, "not-allowed");
            return true;
        }

        clan.createCustomRank(args[0], args[1]);
        sendMessage(player, "c", Replace.of("role", args[0]),
                Replace.of("display", args[1]));
        return true;
    }
}
