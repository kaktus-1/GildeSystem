package de.cribemc.cribeclan.redis.pub;

import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.commands.subcommands.impl.InviteSubCommand;
import de.cribemc.cribeclan.redis.RedisPubSub;
import de.cribemc.cribeclan.utils.ChatUtils;
import de.cribemc.cribeclan.utils.Replace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InviteTimeUpPubSub extends RedisPubSub {
    @Override
    public void onReceive(ClanRegistry clanRegistry, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        Player receiver = Bukkit.getPlayer(args[1]);

        Clan clanFromName = clanRegistry.getClanFromName(args[2]);
        InviteSubCommand.invited.remove(clanFromName);

        if (player != null)
            ChatUtils.sendMessage(player, "commands.invite.f", Replace.of("player", args[1]));
        if (receiver != null)
            ChatUtils.sendMessage(receiver, "commands.invite.g", Replace.of("clan", args[2]));
    }
}
