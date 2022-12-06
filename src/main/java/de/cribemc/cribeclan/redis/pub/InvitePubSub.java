package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.commands.subcommands.impl.InviteSubCommand;
import de.cribemc.cribeclan.redis.RedisPubSub;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InvitePubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Clan clanFromName = clanRegistry.getClanFromName(args[0]);
        InviteSubCommand.invited.put(clanFromName, args[1]);

        Player player = Bukkit.getPlayer(args[1]);
        if (player != null) {
            ChatUtils.sendMessage(player, "commands.invite.i", Replace.of("player", args[2]),
                    Replace.of("clan", clanFromName.getName()));
        }
    }
}
