package de.cribemc.cribeclan.clan;

import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.*;
import de.cribemc.cribeclan.utils.ChatUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final RedisRegistry redisRegistry = CribeClan.getInstance().getRedisRegistry();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<CustomRank> customRanks = new ArrayList<>();

    public void announce(String msg, UUID... notTo) {
        redisRegistry.send(AnnouncePubSub.class, getName(), msg, Arrays.stream(notTo)
                .map(UUID::toString)
                .collect(Collectors.joining(",")));
    }

    public void sendTo(String msg, UUID receiver) {
        redisRegistry.send(SendToPubSub.class, receiver.toString(), msg);
    }

    public void updateTag(String tag) {
        redisRegistry.send(UpdateTagPubSub.class, getName(), tag);
    }

    public void updateName(String name) {
        redisRegistry.send(UpdateTagPubSub.class, getName(), name);
    }

    public void updatePlayerRank(User user, Rank rank) {
        redisRegistry.send(UpdatePlayerRankPubSub.class, getName(), user.getUuid().toString(), rank.name());
    }

    public void setCustomRankForUser(User user, CustomRank customRank) {
        redisRegistry.send(SetCustomRankForUserPubSub.class, getName(), user.getUuid().toString(), customRank == null ? null : customRank.getName());
    }

    public void createCustomRank(String name, String displayName) {
        redisRegistry.send(CreateCustomRankPubSub.class, getName(), name, displayName);
    }

    public void deleteCustomRank(CustomRank customRank) {
        redisRegistry.send(DeleteCustomRankPubSub.class, getName(), customRank.getName());
    }

    public void addPlayer(Player player, Rank rank) {
        redisRegistry.send(AddPlayerPubSub.class, getName(),
                player.getUniqueId().toString(),
                player.getName(),
                rank.name());
    }

    public void addPlayer(Player player) {
        addPlayer(player, Rank.MEMBER);
    }

    public void removePlayer(UUID uuid) {
        redisRegistry.send(RemovePlayerPubSub.class, getName(), uuid.toString());
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
