package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.Rank;
import de.chunkeater32.clan.User;
import de.chunkeater32.commands.subcommands.SubCommand;
import de.chunkeater32.utils.ConfirmHelper;
import de.chunkeater32.utils.Replace;
import org.bukkit.entity.Player;

public class SetNameSubCommand extends SubCommand {
    public SetNameSubCommand() {
        super("setname", "<name>", "Editiere den Namen deines Clans");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Der Name deines Clans ist nun: §b%name%");
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
