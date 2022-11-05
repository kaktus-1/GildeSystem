package de.cribemc.cribeclan.commands.subcommands;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.clan.User;
import de.cribemc.cribeclan.config.Config;
import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.structure.INameable;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.Replace;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public abstract class SubCommand implements INameable {

    private final CribeClan instance = CribeClan.getInstance();
    private final Config config = instance.getConfigRegistry().getByClass(DefaultConfig.class);
    private final ClanRegistry clanRegistry = instance.getClanRegistry();
    private final String name;
    private final String syntax;
    @Setter
    private boolean requiresClan = true;
    @Setter
    @NonNull
    private String description;

    public boolean executeClan(Player player, String[] args, Clan clan, User user) {
        return false;
    }

    public boolean execute(Player player, String[] args) {
        return false;
    }

    public void sendMessage(CommandSender sender, String key, Replace... replaces) {
        ChatUtils.sendMessage(sender, "commands." + name + "." + key, replaces);
    }

    public void sendMessage(CommandSender sender, String key, boolean prefix, Replace... replaces) {
        ChatUtils.sendMessage(sender, "commands." + name + "." + key, prefix, replaces);
    }

    public void addDefault(String key, Object value) {
        config.getCfg().addDefault("commands." + name + "." + key, value);
    }

    public String getString(String key, Replace... replaces) {
        String string = config.getCfg().getString("commands." + name + "." + key);
        for (Replace replace : replaces) {
            string = string.replace(replace.getWhat(), replace.getTo());
        }
        return string;
    }

    public double getDouble(String key) {
        return config.getCfg().getDouble("commands." + name + "." + key);
    }

}
