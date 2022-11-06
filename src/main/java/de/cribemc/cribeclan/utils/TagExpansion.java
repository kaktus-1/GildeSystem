package de.cribemc.cribeclan.utils;

import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.clan.Clan;
import de.cribemc.cribeclan.clan.ClanRegistry;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TagExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "Chunkeater32";
    }

    @Override
    public @NotNull String getAuthor() {
        return "tag";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        ClanRegistry clanRegistry = CribeClan.getInstance().getClanRegistry();
        Clan clanFromUser = clanRegistry.getClanFromUser(player.getUniqueId());
        return clanFromUser == null ? "" : clanFromUser.getTag();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        ClanRegistry clanRegistry = CribeClan.getInstance().getClanRegistry();
        Clan clanFromUser = clanRegistry.getClanFromUser(player.getUniqueId());
        return clanFromUser == null ? "" : clanFromUser.getTag();
    }
}
