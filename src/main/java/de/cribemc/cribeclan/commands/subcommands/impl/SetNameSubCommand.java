package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.ConfirmHelper;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class SetNameSubCommand extends SubCommand {
    public SetNameSubCommand() {
        super("setname", "<name>", "Editiere den Namen deiner Gilde");
        addDefault("a", "§7Dieser Befehl ist dem Gilden-Inhaber vorbehalten!");
        addDefault("b", "§7Der Name deiner Gilde ist nun: §b%name%");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (args.length != 1)
            return false;

        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        // TODO: MYSQL TIME CHECK

        if (Clan.isNotAllowedName(player, args[0]))
            return true;

        ConfirmSubCommand.requestConfirm(player, new ConfirmHelper(this, args, clan));
        return true;
    }

    public void onConfirm(Player player, ConfirmHelper confirmHelper) {
        String[] args = confirmHelper.getArgs();
        Clan clan = confirmHelper.getClan();

        // TODO: MYSQL TIME LIMIT

        clan.updateName(args[0]);
        sendMessage(player, "b", Replace.of("name", args[0]));
    }
}
