package de.chunkeater32.commands;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.ClanRegistry;
import de.chunkeater32.commands.subcommands.SubCommand;
import de.chunkeater32.commands.subcommands.SubCommandRegistry;
import de.chunkeater32.cribeclan.CribeClan;
import de.chunkeater32.utils.ChatUtils;
import de.chunkeater32.utils.Replace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClanCommand implements CommandExecutor {

    private final SubCommandRegistry subCommandRegistry = CribeClan.getInstance().getSubCommandRegistry();
    private final ClanRegistry clanRegistry = CribeClan.getInstance().getClanRegistry();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cribe.clans")) {
            ChatUtils.sendMessage(sender, "no-perm");
            return false;
        }

        if (args.length == 0) {
            List<SubCommand> objects = subCommandRegistry.getObjects().stream()
                    .filter(subCommand -> sender.hasPermission("cribe.clans." + subCommand.getName()))
                    .collect(Collectors.toList());

            for (SubCommand object : objects) {
                ChatUtils.sendMessage(sender, "syntax", Replace.of("command", "/clan " + object.getName()),
                        Replace.of("description", object.getDescription()),
                        Replace.of("syntax", "/clan " + object.getName() + " " + object.getSyntax()));
            }

            return true;
        }

        SubCommand byName = subCommandRegistry.getByName(args[0]);

        if (byName == null) {
            ChatUtils.sendMessage(sender, "not-found", Replace.of("command", args[0]));
            return false;
        }

        if (!sender.hasPermission("cribe.clans." + byName.getName())) {
            ChatUtils.sendMessage(sender, "no-perm");
            return false;
        }

        if (!(sender instanceof Player))
            return false;

        if (byName.isRequiresClan()) {
            Clan clanFromUser = clanRegistry.getClanFromUser(((Player) sender));

            if (clanFromUser == null) {
                ChatUtils.sendMessage(sender, "not-member");
                return false;
            }

            if (!byName.executeClan((Player) sender, Arrays.copyOfRange(args, 1, args.length), clanFromUser)) {
                ChatUtils.sendMessage(sender, "syntax", Replace.of("command", "/clan " + byName.getName()),
                        Replace.of("description", byName.getDescription()),
                        Replace.of("syntax", "/clan " + byName.getName() + " " + byName.getSyntax()));
                return false;
            }
            return true;
        }

        if (!byName.execute((Player) sender, Arrays.copyOfRange(args, 1, args.length))) {
            ChatUtils.sendMessage(sender, "syntax", Replace.of("command", "/clan " + byName.getName()),
                    Replace.of("description", byName.getDescription()),
                    Replace.of("syntax", "/clan " + byName.getName() + " " + byName.getSyntax()));
            return false;
        }
        return true;
    }
}
