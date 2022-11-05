package de.cribemc.cribeclan.commands;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.commands.subcommands.SubCommandRegistry;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GildeCommand implements CommandExecutor {

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
                ChatUtils.sendMessage(sender, "syntax", Replace.of("command", "/gilde " + object.getName()),
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

            User user = clanFromUser.getUser(((Player) sender).getUniqueId());

            if (!byName.executeClan((Player) sender, Arrays.copyOfRange(args, 1, args.length), clanFromUser, user)) {
                ChatUtils.sendMessage(sender, "syntax", Replace.of("command", "/gilde " + byName.getName()),
                        Replace.of("description", byName.getDescription()),
                        Replace.of("syntax", "/clan " + byName.getName() + " " + byName.getSyntax()));
                return false;
            }
            return true;
        }

        if (!byName.execute((Player) sender, Arrays.copyOfRange(args, 1, args.length))) {
            ChatUtils.sendMessage(sender, "syntax", Replace.of("command", "/gilde " + byName.getName()),
                    Replace.of("description", byName.getDescription()),
                    Replace.of("syntax", "/clan " + byName.getName() + " " + byName.getSyntax()));
            return false;
        }
        return true;
    }
}
