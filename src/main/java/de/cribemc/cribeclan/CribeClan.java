package de.cribemc.cribeclan;

import de.cribemc.cribeclan.addon.GS;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.commands.GildeCommand;
import de.cribemc.cribeclan.commands.subcommands.SubCommandRegistry;
import de.cribemc.cribeclan.config.ConfigRegistry;
import de.cribemc.cribeclan.listener.AsyncPlayerChatListener;
import de.cribemc.cribeclan.mysql.MySQLRegistry;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.utils.TagExpansion;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CribeClan extends JavaPlugin {

    @Getter
    private static CribeClan instance;

    private MySQLRegistry mySQLRegistry;
    private RedisRegistry redisRegistry;
    private ConfigRegistry configRegistry;
    private ClanRegistry clanRegistry;
    private SubCommandRegistry subCommandRegistry;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        configRegistry = new ConfigRegistry();
        clanRegistry = new ClanRegistry();
        mySQLRegistry = new MySQLRegistry();
        redisRegistry = new RedisRegistry();
        subCommandRegistry = new SubCommandRegistry();

        GS.initialize();

        getCommand("gilde").setExecutor(new GildeCommand());

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TagExpansion().register();
        }
    }

    @Override
    public void onDisable() {
        getRedisRegistry().destroy();
    }
}
