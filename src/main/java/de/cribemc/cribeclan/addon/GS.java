package de.cribemc.cribeclan.addon;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.PlotId;
import com.plotsquared.core.plot.world.PlotAreaManager;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.Rank;
import de.cribemc.cribeclan.config.Config;
import de.cribemc.cribeclan.config.ConfigRegistry;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GS {

    private static PlotAPI plotAPI;

    public static void initialize() {
        Plugin plotSquared = Bukkit.getPluginManager().getPlugin("PlotSquared");
        if (plotSquared == null || !plotSquared.isEnabled())
            return;

        plotAPI = new PlotAPI();

        CribeClan instance = CribeClan.getInstance();

        ConfigRegistry configRegistry = instance.getConfigRegistry();

        Config config = new Config("gs");
        configRegistry.register(config);

        FileConfiguration cfg = config.getCfg();

        cfg.addDefault("prefix", "§6§lGilde §8• ");
        cfg.addDefault("a", "§7Gilden-Grundstück erfolgreich hinzugefügt!");
        cfg.addDefault("b", "§cGilden-Grundstück erfolgreich entfernt!");
        cfg.addDefault("c", "§7Deine Gilde hat noch kein eigenes Grundstück! Bitte den Gilden-Inhaber, ein Gilden-Grundstück zu erstellen!");
        cfg.addDefault("d", "§7Du befindest dich in keinem Clan.");
        cfg.addDefault("f", "§7Du musst dich auf deinem eigenen Grundstück befinden, um diese Aktion durchführen zu können!");
        cfg.addDefault("g", "§7Du hast keine Berechtigung diesen Befehl auszuführen.");
        cfg.addDefault("h", "§7Bitte benutze /gildengs <set/remove/tp>");
        cfg.addDefault("i", "§7Du musst Inhaber einer Gilde sein um diesen Befehl auszuführen.");
        cfg.addDefault("j", "§7Erfolgreich zum Gilden-Grundstück teleportiert.");
        cfg.addDefault("k", "§7Die Gilde %clan% existiert nicht.");
        cfg.addDefault("l", "§7Diese Gilde hat noch kein Gilden Grundstück.");
        cfg.addDefault("m", "§7Du musst Gilden Inhaber sein für diese Aktion.");
        cfg.addDefault("plotworld", "Plotwelt");
        cfg.options().copyDefaults(true);

        config.save();

        ClanRegistry clanRegistry = instance.getClanRegistry();

        Bukkit.getCommandMap().register("gildengs", new BukkitCommand("gildengs") {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!(sender instanceof Player player))
                    return false;

                if (args.length != 1 && args.length != 2) {
                    sendMessage(player, "h");
                    return true;
                }

                if (args.length == 2 && !args[0].equalsIgnoreCase("tp")) {
                    sendMessage(player, "h");
                    return true;
                }

                Clan clanFromUser = clanRegistry.getClanFromUser(player);

                switch (args[0].toLowerCase()) {
                    case "set" -> {
                        if (checkPerm(player, "set")) return true;

                        if (clanFromUser == null) {
                            sendMessage(player, "i");
                            return true;
                        }

                        if (clanFromUser.getUser(player).getRank() != Rank.OWNER) {
                            sendMessage(player, "m");
                            return true;
                        }

                        Plot plot = getPlot(player.getLocation());
                        if (plot == null || !plot.getOwners().contains(player.getUniqueId())) {
                            sendMessage(player, "f");
                            return true;
                        }
                        cfg.addDefault(clanFromUser.getName(), plot.getId().toString());
                        sendMessage(player, "a");
                        return true;
                    }
                    case "remove" -> {
                        if (checkPerm(player, "remove")) return true;

                        if (clanFromUser == null) {
                            sendMessage(player, "i");
                            return true;
                        }

                        if (clanFromUser.getUser(player).getRank() != Rank.OWNER) {
                            sendMessage(player, "m");
                            return true;
                        }

                        if (cfg.get(clanFromUser.getName()) == null) {
                            sendMessage(player, "c");
                            return true;
                        }

                        cfg.addDefault(clanFromUser.getName(), null);
                        sendMessage(player, "b");
                        return true;
                    }
                    case "tp" -> {
                        if (args.length == 1) {
                            if (checkPerm(player, "tp")) return true;

                            if (clanFromUser == null) {
                                sendMessage(player, "d");
                                return true;
                            }

                            tp(player, clanFromUser, cfg, true);
                            return true;
                        }

                        if (checkPerm(player, "tp.other")) return true;

                        Clan clanFromName = clanRegistry.getClanFromName(args[1]);

                        if (clanFromName == null) {
                            sendMessage(player, "k", Replace.of("clan", args[1]));
                            return true;
                        }

                        tp(player, clanFromName, cfg, false);
                        return true;
                    }
                }

                sendMessage(player, "h");
                return true;
            }
        });
    }

    private static boolean checkPerm(Player player, String sub) {
        boolean b = player.hasPermission("clan.clangs." + sub);

        if (!b) {
            sendMessage(player, "g");
            return true;
        }

        return false;
    }

    private static void tp(Player player, Clan clan, FileConfiguration cfg, boolean a) {
        String o = cfg.getString(clan.getName());
        if (o == null) {
            if (a)
                sendMessage(player, "c");
            else
                sendMessage(player, "l");
            return;
        }

        PlotArea plotArea = getPlotArea(new Location(Bukkit.getWorld("Plotwelt"), 0, 0, 0));
        String[] split = o.split(";");
        Plot plot = plotArea.getPlot(PlotId.of(Integer.parseInt(split[0]), Integer.parseInt(split[1])));

        plot.teleportPlayer(plotAPI.wrapPlayer(player.getUniqueId()), aBoolean -> {
        });
        sendMessage(player, "j");
    }

    public static void update(String oldName, String newName) {
        Plugin plotSquared = Bukkit.getPluginManager().getPlugin("PlotSquared");
        if (plotSquared == null || !plotSquared.isEnabled())
            return;

        Config gs = CribeClan.getInstance().getConfigRegistry().getByName("gs");

        Object o = gs.getCfg().get(oldName);

        if (o == null) return;

        gs.getCfg().addDefault(oldName, null);
        gs.getCfg().addDefault(newName, o);

        gs.save();
    }

    private static Plot getPlot(Location location) {
        PlotArea plotArea = getPlotArea(location);

        if (plotArea == null) return null;

        return plotArea.getPlot(BukkitUtil.adapt(location));
    }

    private static PlotArea getPlotArea(Location location) {
        PlotSquared plotSquared = plotAPI.getPlotSquared();
        PlotAreaManager plotAreaManager = plotSquared.getPlotAreaManager();

        return plotAreaManager.getPlotArea(BukkitUtil.adapt(location));
    }

    private static void sendMessage(CommandSender sender, String key, Replace... replaces) {
        Config gs = CribeClan.getInstance().getConfigRegistry().getByName("gs");
        FileConfiguration cfg = gs.getCfg();

        String string = cfg.getString(key);
        for (Replace replace : replaces) {
            string = string.replace(replace.getWhat(), replace.getTo());
        }

        sender.sendMessage(cfg.getString("prefix") + string);
    }
}
