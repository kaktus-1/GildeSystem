package de.cribemc.cribeclan.clan;

import co.aikar.idb.DB;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.redis.RedisRegistry;
import de.cribemc.cribeclan.redis.pub.*;
import de.cribemc.cribeclan.utils.ChatUtils;
import lombok.*;
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

    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<CustomRank> customRanks = new ArrayList<>();

    private RedisRegistry getRedisRegistry() {
        return CribeClan.getInstance().getRedisRegistry();
    }

    public void announce(String msg, UUID... notTo) {
        getRedisRegistry().send(AnnouncePubSub.class, getName(), msg, Arrays.stream(notTo)
                .map(UUID::toString)
                .collect(Collectors.joining(",")));
    }

    public void sendTo(String msg, UUID receiver) {
        getRedisRegistry().send(SendToPubSub.class, receiver.toString(), msg);
    }

    @SneakyThrows
    public void updateTag(String tag) {
        DB.executeUpdate("UPDATE `clans` SET `tag` = ? WHERE `name` = ?",
                tag, getName());
        getRedisRegistry().send(UpdateTagPubSub.class, getName(), tag);
    }

    @SneakyThrows
    public void updateName(String name) {
        DB.executeUpdate("UPDATE `clans` SET `name` = ? WHERE `name` = ?",
                name, getName());
        DB.executeUpdate("UPDATE `customranks` SET `clan` = ? WHERE `clan` = ?",
                name, getName());
        DB.executeUpdate("UPDATE `users` SET `clan` = ? WHERE `clan` = ?",
                name, getName());
        DB.executeUpdate("UPDATE `tagcooldown` SET `clan` = ? WHERE `clan` = ?",
                name, getName());
        getRedisRegistry().send(UpdateNamePubSub.class, getName(), name);
    }

    @SneakyThrows
    public void updatePlayerRank(User user, Rank rank) {
        DB.executeUpdate("UPDATE `users` SET `rank` = ? WHERE `uuid` = ?",
                rank.name(), user.getUuid().toString());
        getRedisRegistry().send(UpdatePlayerRankPubSub.class, getName(), user.getUuid().toString(), rank.name());
    }

    @SneakyThrows
    public void setCustomRankForUser(User user, CustomRank customRank) {
        DB.executeUpdate("UPDATE `users` SET `customrank` = ? WHERE `uuid` = ?",
                customRank == null ? null : customRank.getName(), user.getUuid().toString());
        getRedisRegistry().send(SetCustomRankForUserPubSub.class, getName(), user.getUuid().toString(), customRank == null ? null : customRank.getName());
    }

    @SneakyThrows
    public void createCustomRank(String name, String displayName) {
        DB.executeInsert("INSERT INTO `customranks` (`clan`, `name`, `displayname`) VALUES (?, ?, ?)",
                getName(), name, displayName);
        getRedisRegistry().send(CreateCustomRankPubSub.class, getName(), name, displayName);
    }

    @SneakyThrows
    public void deleteCustomRank(CustomRank customRank) {
        DB.executeUpdate("DELETE FROM `customranks` WHERE `clan` = ? AND `name` = ?",
                getName(), customRank.getName());
        getRedisRegistry().send(DeleteCustomRankPubSub.class, getName(), customRank.getName());
    }

    @SneakyThrows
    public void addPlayer(Player player, Rank rank) {
        String string = player.getUniqueId().toString();
        String name1 = player.getName();
        String name2 = rank.name();
        DB.executeInsert("INSERT INTO `users` (`uuid`, `name`, `clan`, `rank`, `customrank`, `clanchat`) VALUES (?, ?, ?, ?, ?, ?)",
                string, name1, getName(), name2, null, "false");
        getRedisRegistry().send(AddPlayerPubSub.class, getName(),
                string,
                name1,
                name2);
    }

    public void addPlayer(Player player) {
        addPlayer(player, Rank.MEMBER);
    }

    @SneakyThrows
    public void removePlayer(UUID uuid) {
        DB.executeUpdate("DELETE FROM `users` WHERE `clan` = ? AND `uuid` = ?",
                getName(), uuid.toString());
        getRedisRegistry().send(RemovePlayerPubSub.class, getName(), uuid.toString());
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
