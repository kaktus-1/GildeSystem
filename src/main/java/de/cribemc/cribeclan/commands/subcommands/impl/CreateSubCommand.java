package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.Replace;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class CreateSubCommand extends SubCommand {
    public CreateSubCommand() {
        super("create", "<name> <tag>", "Erstellt einen neue Gilde (benötigt 25.000$)");
        addDefault("costs", 25000d);
        addDefault("regex", "(?i)[^A-Z0-9ÄÖÜ§]");
        addDefault("a", "§7Du benötigst §625.000$§7, um diese Gilde zu erstellen.");
        addDefault("b", "§7Du hast §b%name% §7mit dem Gilden-Tag §b%tag% §7erfolgreich erstellt!");
        addDefault("c", "§7Der gewünschte Gilden-Name ist bereits vergeben!");
        addDefault("d", "§7Der gewünschte Gilden-Tag ist bereits vergeben!");
        addDefault("e", "§7Du darfst keine Farbcodes verwenden.");
        addDefault("f", "§7Farbcodes dürfen nur in den Gilden Tag!");
        setRequiresClan(false);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2)
            return false;

        double costs = getDouble("costs");

        CribeClan instance = CribeClan.getInstance();
        Economy economy = instance.getEconomy();

        if (getClanRegistry().getClanFromUser(player) != null) {
            ChatUtils.sendMessage(player, "already-member");
            return true;
        }

        if (!economy.has(player, costs)) {
            sendMessage(player, "a");
            return true;
        }

        if (args[1].contains("&") && !player.hasPermission("cribe.clans.coloredtag")) {
            sendMessage(player, "e");
            return true;
        }

        if (args[0].contains("&")) {
            sendMessage(player, "f");
            return true;
        }

        args[1] = args[1].replace("&", "§");

        Pattern regex = Pattern.compile(getString("regex"));

        if (regex.matcher(args[0]).find() || regex.matcher(args[1]).find()) {
            ChatUtils.sendMessage(player, "not-allowed");
            return true;
        }

        if (Clan.isNotAllowedName(player, args[0]))
            return true;

        if (Clan.isNotAllowedTag(player, args[1]))
            return true;

        if (getClanRegistry().getClanFromName(args[0]) != null) {
            sendMessage(player, "c");
            return true;
        }

        if (getClanRegistry().getClanFromTag(args[1]) != null) {
            sendMessage(player, "d");
            return true;
        }

        if (!economy.withdrawPlayer(player, costs).transactionSuccess()) {
            sendMessage(player, "a");
            return true;
        }
        getClanRegistry().createClan(player, args[0], args[1]);
        sendMessage(player, "b", Replace.of("name", args[0]),
                Replace.of("tag", args[1]));
        return true;
    }
}
