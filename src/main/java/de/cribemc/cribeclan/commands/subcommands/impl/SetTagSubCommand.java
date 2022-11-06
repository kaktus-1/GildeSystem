package de.cribemc.cribeclan.commands.subcommands.impl;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.ConfirmHelper;
import de.cribemc.cribeclan.utils.Replace;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

public class SetTagSubCommand extends SubCommand {

    public SetTagSubCommand() {
        super("settag", "<tag>", "Editiere den Tag deiner Gilde");
        addDefault("a", "§7Der gewünschte Gilden-Tag ist bereits vergeben!");
        addDefault("b", "§7Du darfst keine Farbcodes verwenden.");
        addDefault("c", "§7Der Tag deiner Gilde ist nun: %tag%");
        addDefault("d", "§7Dieser Befehl ist dem Gilden-Inhaber vorbehalten!");
        addDefault("e", "§7Bitte warte 7 Tage bevor du deinen Gilden Namen erneut ändern kannst.");
        addDefault("time-in-milli", "604800000");
    }

    @Override
    @SneakyThrows
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "d");
            return true;
        }

        if (args.length != 1)
            return false;

        DbRow firstRow = DB.getFirstRow("SELECT * FROM `tagcooldown` WHERE `clan` = ?", clan.getName());

        if (firstRow != null && !firstRow.isEmpty() && System.currentTimeMillis() < Long.parseLong(firstRow.getString("time"))) {
            sendMessage(player, "e");
            return true;
        }

        if (firstRow != null)
            DB.executeUpdate("DELETE FROM `tagcooldown` WHERE `clan` = ?", clan.getName());

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

        ConfirmSubCommand.requestConfirm(player, new ConfirmHelper(this, args, clan));
        return true;
    }

    @SneakyThrows
    public void onConfirm(Player player, ConfirmHelper confirmHelper) {
        String[] args = confirmHelper.getArgs();
        Clan clan = confirmHelper.getClan();

        DB.executeInsert("INSERT INTO `tagcooldown` (`clan`, `time`) VALUES (?, ?)",
                clan.getName(), (System.currentTimeMillis() + (Long.parseLong(getString("time-in-milli")))));

        clan.updateTag(args[0]);
        sendMessage(player, "c", Replace.of("tag", args[0]));
    }
}
