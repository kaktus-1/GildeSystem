package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.ConfirmHelper;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ConfirmSubCommand extends SubCommand {

    public ConfirmSubCommand() {
        super("confirm", "", "Bestätigt eine bestimmte Aktion");
        addDefault("a", "§7Bitte bestätige deine Eingabe mit §e/clan confirm");
        addDefault("b", "§7Bitte warte einen Moment bis du erneut eine Aktion bestätigen kannst.");
        addDefault("c", "§7Es gibt keine Aktion zu bestätigen.");
        addDefault("d", "§7Zeit um die Aktion zu bestätigen ist abgelaufen.");
    }

    private static final HashMap<UUID, ConfirmHelper> confirms = new HashMap<>();

    @Override
    @SneakyThrows
    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        UUID uniqueId = player.getUniqueId();

        if (!confirms.containsKey(uniqueId)) {
            sendMessage(player, "c");
            return true;
        }

        ConfirmHelper confirmHelper = confirms.get(uniqueId);
        confirms.remove(uniqueId);
        confirmHelper.setDone(true);
        confirmHelper.getSubCommand()
                .getClass()
                .getDeclaredMethod("onConfirm", Player.class, confirmHelper.getClass())
                .invoke(confirmHelper.getSubCommand(), player, confirmHelper);
        return true;
    }

    public static void requestConfirm(Player player, ConfirmHelper confirmHelper) {
        UUID uuid = player.getUniqueId();

        if (confirms.containsKey(uuid)) {
            ChatUtils.sendMessage(player, "commands.confirm.b");
            return;
        }

        ChatUtils.sendMessage(player, "commands.confirm.a");
        confirms.put(uuid, confirmHelper);

        Bukkit.getScheduler().runTaskLater(CribeClan.getInstance(), () -> {
            if (!confirmHelper.isDone()) {
                confirms.remove(uuid);
                ChatUtils.sendMessage(player, "commands.confirm.d");
            }
        }, 20 * 30);
    }
}
