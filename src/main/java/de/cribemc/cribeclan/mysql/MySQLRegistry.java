package de.cribemc.cribeclan.mysql;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.Database;
import co.aikar.idb.DbRow;
import co.aikar.idb.PooledDatabaseOptions;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.clan.*;
import de.cribemc.cribeclan.config.impl.DefaultConfig;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class MySQLRegistry {

    @SneakyThrows
    public MySQLRegistry() {
        CribeClan instance = CribeClan.getInstance();
        FileConfiguration cfg = instance.getConfigRegistry().getByClass(DefaultConfig.class).getCfg();

        PooledDatabaseOptions recommendedOptions = BukkitDB.getRecommendedOptions(instance, cfg.getString("mysql.user"),
                cfg.getString("mysql.password"),
                cfg.getString("mysql.database"),
                cfg.getString("mysql.host") + ":" + cfg.getInt("mysql.port"));
        Database hikariDatabase = BukkitDB.createHikariDatabase(instance, recommendedOptions);

        hikariDatabase.executeUpdate("CREATE TABLE IF NOT EXISTS `clans` (`name` TEXT NOT NULL, " +
                "`tag` TEXT NOT NULL)");
        hikariDatabase.executeUpdate("CREATE TABLE IF NOT EXISTS `users` (`uuid` TEXT NOT NULL, " +
                "`name` TEXT NOT NULL, " +
                "`clan` TEXT NOT NULL, " +
                "`rank` TEXT NOT NULL, " +
                "`customrank` TEXT, " +
                "`clanchat` TEXT NOT NULL)");
        hikariDatabase.executeUpdate("CREATE TABLE IF NOT EXISTS `customranks` (`clan` TEXT NOT NULL, " +
                "`name` TEXT NOT NULL, " +
                "`displayname` TEXT NOT NULL)");

        ClanRegistry clanRegistry = instance.getClanRegistry();

        for (DbRow result : hikariDatabase.getResults("SELECT * FROM `clans`")) {
            String name = result.getString("name");
            String tag = result.getString("tag");

            clanRegistry.getClans().add(new Clan(name, tag));
        }

        for (DbRow result : hikariDatabase.getResults("SELECT * FROM `customranks`")) {
            String clan = result.getString("clan");
            String name = result.getString("name");
            String displayName = result.getString("displayname");

            clanRegistry.getClanFromName(clan).getCustomRanks().add(new CustomRank(name, displayName));
        }

        for (DbRow result : hikariDatabase.getResults("SELECT * FROM `users`")) {
            UUID user = UUID.fromString(result.get("uuid"));
            String name = result.getString("name");
            Clan clan = clanRegistry.getClanFromName(result.getString("clan"));
            Rank rank = Rank.valueOf(result.getString("rank"));
            Object customRankObject = result.getOrDefault("customrank", null);
            CustomRank customRank = customRankObject == null ? null : clan.getCustomRank(((String) customRankObject));
            boolean clanChat = Boolean.parseBoolean(result.getString("clanchat"));

            User user1 = new User(user, name, clan, rank);
            user1.setCustomRank(customRank);
            user1.setClanChat(clanChat);

            clan.getUsers().add(user1);
        }

    }
}
