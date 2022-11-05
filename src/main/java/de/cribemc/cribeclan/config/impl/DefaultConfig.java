package de.cribemc.cribeclan.config.impl;

import de.cribemc.cribeclan.config.Config;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

@Getter
public class DefaultConfig extends Config {
    public DefaultConfig() {
        super("config");
    }

    private String prefix;
    private int clanNameLength;
    private int clanTagLength;
    private int clanMaxMembers;

    @Override
    public void onStart() {
        FileConfiguration cfg = getCfg();

        cfg.addDefault("prefix", "§6§lClan §8• ");
        cfg.addDefault("redis.host", "127.0.0.1");
        cfg.addDefault("redis.port", 6379);
        cfg.addDefault("redis.user", "root");
        cfg.addDefault("redis.password", null);
        cfg.addDefault("clan.name_length", 20);
        cfg.addDefault("clan.tag_length", 7);
        cfg.addDefault("clan.max_members", 20);
        cfg.addDefault("syntax", Arrays.asList("§e%command%",
                "   §7=> §7%description%",
                "   §7=> §e%syntax%",
                "§7"));
        cfg.addDefault("not-found", "§7Der Unterbefehl §e%command% §7existiert nicht.");
        cfg.addDefault("no-perm", "§7Keine Berechtigung!");
        cfg.addDefault("not-allowed", Arrays.asList("§7Deine Eingabe enthält unerlaubte Zeichen!",
                "§7Erlaubte Zeichen: §bA-Z§8, §bÄ§8, §bÜ§8, §bÖ§8, §b0-9"));
        cfg.addDefault("not-member", "§7Du bist aktuell nicht Mitglied eines Clans!");
        cfg.addDefault("already-member", "§7Du bist bereits Mitglied eines Clans!");
        cfg.addDefault("clan-tag-too-long", "§7Der Clan Tag darf maximal 7 Zeichen lang sein.");
        cfg.addDefault("clan-name-too-long", "§7Der Clan Name darf maximal 20 Zeichen lang sein.");
        cfg.addDefault("prefix-owner", "§4");
        cfg.addDefault("prefix-mod", "§a");
        cfg.addDefault("prefix-member", "§7");
        cfg.options().copyDefaults(true);

        save();

        prefix = cfg.getString("prefix");
        clanNameLength = cfg.getInt("clan.name_length");
        clanTagLength = cfg.getInt("clan.tag_length");
        clanMaxMembers = cfg.getInt("clan.max_members");
    }
}
