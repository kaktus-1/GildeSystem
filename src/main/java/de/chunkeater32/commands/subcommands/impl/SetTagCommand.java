package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.commands.subcommands.SubCommand;
import de.chunkeater32.utils.Replace;
import org.bukkit.entity.Player;

public class SetTagCommand extends SubCommand {
    public SetTagCommand() {
        super("settag", "<tag>", "Editiere den Tag deines Clans");
        addDefault("a", "§7Der gewünschte Clan-Tag ist bereits vergeben!");
        addDefault("b", "§7Du darfst keine Farbcodes verwenden.");
        addDefault("c", "§7Der Tag deines Clans ist nun: %tag%");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan) {
        if (args.length != 1)
            return false;

        // TODO: MYSQL TIME CHECK

        Clan clanFromTag = getClanRegistry().getClanFromTag(args[0]);

        if (clanFromTag != null) {
            sendMessage(player, "a");
            return true;
        }

        if (args[0].contains("&") && !player.hasPermission("cribe.clans.coloredtag")) {
            sendMessage(player, "b");
            return true;
        }

        args[0] = args[0].replace("&", "§");

        if (Clan.isNotAllowedTag(player, args[0]))
            return true;

        clan.updateTag(args[0]);
        sendMessage(player, "c", Replace.of("tag", args[0]));
        return true;
    }
}
