package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.CustomRank;
import de.chunkeater32.clan.Rank;
import de.chunkeater32.clan.User;
import de.chunkeater32.commands.subcommands.SubCommand;
import de.chunkeater32.utils.Replace;
import org.bukkit.entity.Player;

public class DeleteRoleSubCommand extends SubCommand {
    public DeleteRoleSubCommand() {
        super("deleterole", "<role>", "Löscht eine bestimmte Clan-Rolle");
        addDefault("a", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("b", "§7Es existiert kein Rolle mit diesem Namen.");
        addDefault("c", "§7Du hast die Rolle §b%role% §7(§b%display%§7) erfolgreich gelöscht.");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "a");
            return true;
        }

        if (args.length != 1)
            return false;

        CustomRank customRank = clan.getCustomRank(args[0]);
        if (customRank == null) {
            sendMessage(player, "b");
            return true;
        }

        clan.deleteCustomRank(customRank);
        sendMessage(player, "c", Replace.of("role", customRank.getName()),
                Replace.of("display", customRank.getDisplayName()));
        return true;
    }
}
