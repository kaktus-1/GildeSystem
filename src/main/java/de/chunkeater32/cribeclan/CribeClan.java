package de.chunkeater32.cribeclan;

import de.chunkeater32.clan.ClanRegistry;
import de.chunkeater32.commands.ClanCommand;
import de.chunkeater32.commands.subcommands.SubCommandRegistry;
import de.chunkeater32.config.ConfigRegistry;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CribeClan extends JavaPlugin {

    @Getter
    private static CribeClan instance;

    private ConfigRegistry configRegistry;
    private ClanRegistry clanRegistry;
    private SubCommandRegistry subCommandRegistry;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        configRegistry = new ConfigRegistry();
        clanRegistry = new ClanRegistry();
        subCommandRegistry = new SubCommandRegistry();

        getCommand("clan").setExecutor(new ClanCommand());

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }

    @Override
    public void onDisable() {

    }
}
