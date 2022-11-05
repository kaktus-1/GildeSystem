package de.cribemc.cribeclan.commands.subcommands.impl;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.commands.subcommands.SubCommand;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListSubCommand extends SubCommand {
    public ListSubCommand() {
        super("list", "[gilde]", "Auflistung der Mitglieder der eigenen/einer anderen Gilde");
        addDefault("a", "§7Konnte keine Gilde mit dem Namen/Tag §e%name% §7finden.");
        addDefault("own", "§6Gilden §8| §7Mitglieder:");
        addDefault("other", "§6Gilden §8| §7Mitglieder von §b%name%§7 (§b%tag%§7):");
        addDefault("format", "§8• %role% §8| %prefix%%player%");
        addDefault("list", Arrays.asList("%info%",
                "§7",
                "§7Inhaber:",
                "%owner%",
                "§7",
                "§7Moderation:",
                "%mods%",
                "§7",
                "§7Mitglieder:",
                "%members%"));
        addDefault("none", "§7Keiner");
        setRequiresClan(false);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            Clan clanFromUser = getClanRegistry().getClanFromUser(player);

            if (clanFromUser == null) {
                ChatUtils.sendMessage(player, "not-member");
                return true;
            }

            printClanInfo(player, clanFromUser);
            return true;
        }

        Clan clan;

        String arg = args[0].replace("&", "§");

        Clan clanFromName = getClanRegistry().getClanFromName(arg);
        if (clanFromName == null) {
            Clan clanFromTag = getClanRegistry().getClanFromTag(arg);
            if (clanFromTag == null) {
                sendMessage(player, "a", Replace.of("name", arg));
                return true;
            }
            clan = clanFromTag;
        } else {
            clan = clanFromName;
        }

        printClanInfo(player, clan);
        return true;
    }

    private void printClanInfo(Player to, Clan clan) {
        boolean isSame = clan.getUser(to) != null;

        sendMessage(to, "list", false,
                Replace.of("info", getString(isSame ? "own" : "other",
                        Replace.of("name", clan.getName()),
                        Replace.of("tag", clan.getTag()))),
                Replace.of("owner",
                        format(clan.getUsersByRank(Rank.OWNER))),
                Replace.of("mods",
                        format(clan.getUsersByRank(Rank.MODERATOR))),
                Replace.of("members",
                        format(clan.getUsersByRank(Rank.MEMBER))));

    }

    private String format(List<User> users) {

        String format = users.stream().map(user -> {
            Rank rank = user.getRank();

            return getString("format", Replace.of("role", user.getPrefix()),
                    Replace.of("prefix", user.getRankPrefix()),
                    Replace.of("player", user.getName()));
        }).collect(Collectors.joining("\n"));

        if (format.isEmpty() || format.isBlank())
            format = getString("none");

        return format;
    }
}
