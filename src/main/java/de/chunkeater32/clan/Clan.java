package de.chunkeater32.clan;

import de.chunkeater32.config.impl.DefaultConfig;
import de.chunkeater32.cribeclan.CribeClan;
import de.chunkeater32.utils.ChatUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Clan {

    @NonNull
    @Setter
    private String name;
    @NonNull
    @Setter
    private String tag;

    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<CustomRank> customRanks = new ArrayList<>();

    public void announce(String msg, UUID... notTo) {

    }

    public void sendTo(String msg, UUID receiver) {

    }

    public void updateTag(String tag) {
        setTag(tag);
    }

    public void updateName(String name) {
        setName(name);
    }

    public void updatePlayerRank(User user, Rank rank) {
        user.setRank(rank);
    }

    public void updateCustomRank(String name, String displayName) {
        CustomRank customRank = getCustomRank(name);
        customRank.setDisplayName(displayName);
    }

    public CustomRank createCustomRank(String name, String displayName) {
        CustomRank customRank = new CustomRank(name, displayName);
        customRanks.add(customRank);
        return customRank;
    }

    public void deleteCustomRank(CustomRank customRank) {
        customRanks.remove(customRank);
    }

    public void addPlayer(Player player, Rank rank) {
        UUID uniqueId = player.getUniqueId();
        String name = player.getName();

        User user = new User(uniqueId, name, this, rank);

        users.add(user);
    }

    public void addPlayer(Player player) {
        addPlayer(player, Rank.MEMBER);
    }

    public void removePlayer(String name) {
        User user = getUser(name);
        users.remove(user);
    }

    public void removePlayer(UUID uuid) {
        User user = getUser(uuid);
        users.remove(user);
    }

    public CustomRank getCustomRank(String name) {
        return customRanks.stream()
                .filter(customRank -> customRank.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public User getUser(String name) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(UUID uuid) {
        return users.stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    public List<User> getUsersByRank(Rank rank) {
        return users.stream()
                .filter(user -> user.getRank() == rank)
                .collect(Collectors.toUnmodifiableList());
    }

    public static boolean isNotAllowedTag(Player player, String tag) {
        boolean b = ChatColor.stripColor(tag).length() > ((DefaultConfig) CribeClan.getInstance()
                .getConfigRegistry()
                .getByClass(DefaultConfig.class))
                .getClanTagLength();
        if (b) ChatUtils.sendMessage(player, "clan-tag-too-long");
        return b;
    }

    public static boolean isNotAllowedName(Player player, String name) {
        boolean b = name.length() > ((DefaultConfig) CribeClan.getInstance()
                .getConfigRegistry()
                .getByClass(DefaultConfig.class))
                .getClanNameLength();
        if (b) ChatUtils.sendMessage(player, "clan-name-too-long");
        return b;
    }

}
