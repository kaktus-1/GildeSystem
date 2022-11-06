package de.cribemc.cribeclan.commands.subcommands.impl;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import de.cribemc.cribeclan.addon.GS;
import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.ConfirmHelper;
import de.cribemc.cribeclan.utils.Replace;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

public class SetNameSubCommand extends SubCommand {
    public SetNameSubCommand() {
        super("setname", "<name>", "Editiere den Namen deiner Gilde");
        addDefault("a", "§7Dieser Befehl ist dem Gilden-Inhaber vorbehalten!");
        addDefault("b", "§7Der Name deiner Gilde ist nun: §b%name%");
        addDefault("c", "§7Bitte warte 7 Tage bevor du deinen Gilden Namen erneut ändern kannst.");
        addDefault("time-in-milli", "604800000");
    }

    @Override
    @SneakyThrows
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (args.length != 1)
            return false;

        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        DbRow firstRow = DB.getFirstRow("SELECT * FROM `namecooldown` WHERE `clan` = ?", clan.getName());

        if (firstRow != null && !firstRow.isEmpty() && System.currentTimeMillis() < Long.parseLong(firstRow.getString("time"))) {
            sendMessage(player, "c");
            return true;
        }

        if (firstRow != null)
            DB.executeUpdate("DELETE FROM `namecooldown` WHERE `clan` = ?", clan.getName());

        if (Clan.isNotAllowedName(player, args[0]))
            return true;

        ConfirmSubCommand.requestConfirm(player, new ConfirmHelper(this, args, clan));
        return true;
    }

    @SneakyThrows
    public void onConfirm(Player player, ConfirmHelper confirmHelper) {
        String[] args = confirmHelper.getArgs();
        Clan clan = confirmHelper.getClan();

        GS.update(clan.getName(), args[0]);
        clan.updateName(args[0]);

        DB.executeInsert("INSERT INTO `namecooldown` (`clan`, `time`) VALUES (?, ?)",
                clan.getName(), (System.currentTimeMillis() + (Long.parseLong(getString("time-in-milli")))));

        sendMessage(player, "b", Replace.of("name", args[0]));
    }
}
