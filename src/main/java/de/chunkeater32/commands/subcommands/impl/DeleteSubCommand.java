package de.chunkeater32.commands.subcommands.impl;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.Rank;
import de.chunkeater32.clan.User;
import de.chunkeater32.commands.subcommands.SubCommand;
import de.chunkeater32.utils.ConfirmHelper;
import de.chunkeater32.utils.Replace;
import org.bukkit.entity.Player;

public class DeleteSubCommand extends SubCommand {
    public DeleteSubCommand() {
        super("delete", "", "Löscht einen zuvor erstellten Clan");
        addDefault("a", "§7Du hast §b%name% §7erfolgreich aufgelöst!");
        addDefault("b", "§7Dieser Befehl ist dem Clan-Inhaber vorbehalten!");
        addDefault("c", "§7Der Clan wurde aufgelöst.");
    }

    @Override
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        if (user.getRank() != Rank.OWNER) {
            sendMessage(player, "b");
            return true;
        }

        ConfirmSubCommand.requestConfirm(player, new ConfirmHelper(this, args, clan));
        return true;
    }

    public void onConfirm(Player player, ConfirmHelper confirmHelper) {
        Clan clan = confirmHelper.getClan();
        clan.announce(getString("c"), player.getUniqueId());
        getClanRegistry().deleteClan(clan);
        sendMessage(player, "a", Replace.of("name", clan.getName()));
    }
}
