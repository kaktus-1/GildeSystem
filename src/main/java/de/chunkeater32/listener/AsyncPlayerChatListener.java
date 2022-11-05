package de.chunkeater32.listener;

import de.chunkeater32.clan.Clan;
import de.chunkeater32.clan.ClanRegistry;
import de.chunkeater32.clan.User;
import de.chunkeater32.config.impl.DefaultConfig;
import de.chunkeater32.cribeclan.CribeClan;
import de.chunkeater32.utils.Replace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final CribeClan instance = CribeClan.getInstance();
    private final ClanRegistry clanRegistry = instance.getClanRegistry();
    private final FileConfiguration cfg = instance.getConfigRegistry().getByClass(DefaultConfig.class).getCfg();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = clanRegistry.getUser(player);

        if (user == null)
            return;

        if (!user.isClanChat())
            return;

        event.setCancelled(true);

        Clan clan = user.getClan();

        clan.announce(getString("format", Replace.of("role", user.getPrefix()),
                Replace.of("prefix", user.getRankPrefix()),
                Replace.of("player", player.getName()),
                Replace.of("message", event.getMessage())));
    }

    public String getString(String key, Replace... replaces) {
        String string = cfg.getString("commands.chat." + key);
        for (Replace replace : replaces) {
            string = string.replace(replace.getWhat(), replace.getTo());
        }
        return string;
    }

}
