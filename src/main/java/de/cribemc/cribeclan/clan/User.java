package de.cribemc.cribeclan.clan;

import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.CribeClan;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class User {

    private final FileConfiguration cfg = CribeClan.getInstance().getConfigRegistry().getByClass(DefaultConfig.class).getCfg();
    ;
    private final UUID uuid;
    private final String name;
    private final Clan clan;
    @NonNull
    private Rank rank;
    private CustomRank customRank;
    private boolean clanChat = false;

    public String getPrefix() {
        String rank1;
        if (customRank == null)
            rank1 = rank.getPrefix();
        else
            rank1 = customRank.getDisplayName();
        return rank1;
    }

    public String getRankPrefix() {
        String prefix;
        if (rank == Rank.OWNER)
            prefix = cfg.getString("prefix-owner");
        else if (rank == Rank.MODERATOR)
            prefix = cfg.getString("prefix-mod");
        else
            prefix = cfg.getString("prefix-member");
        return prefix;
    }
}
