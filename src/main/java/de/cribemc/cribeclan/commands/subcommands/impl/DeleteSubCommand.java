package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.ConfirmHelper;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

public class DeleteSubCommand extends SubCommand {
    public DeleteSubCommand() {
        super("delete", "", "Löscht eine zuvor erstelle Gilde");
        addDefault("a", "§7Du hast §b%name% §7erfolgreich aufgelöst!");
        addDefault("b", "§7Dieser Befehl ist dem Gilden-Inhaber vorbehalten!");
        addDefault("c", "§7Die Gilde wurde aufgelöst.");
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
