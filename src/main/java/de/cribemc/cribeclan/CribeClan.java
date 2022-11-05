package de.cribemc.cribeclan;

import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.commands.GildeCommand;
import de.cribemc.cribeclan.commands.subcommands.SubCommandRegistry;
import de.cribemc.cribeclan.config.ConfigRegistry;
import de.cribemc.cribeclan.listener.AsyncPlayerChatListener;
import de.cribemc.cribeclan.redis.RedisRegistry;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CribeClan extends JavaPlugin {

    @Getter
    private static CribeClan instance;

    private RedisRegistry redisRegistry;
    private ConfigRegistry configRegistry;
    private ClanRegistry clanRegistry;
    private SubCommandRegistry subCommandRegistry;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        configRegistry = new ConfigRegistry();
        redisRegistry = new RedisRegistry();
        clanRegistry = new ClanRegistry();
        subCommandRegistry = new SubCommandRegistry();

        getCommand("gilde").setExecutor(new GildeCommand());

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
