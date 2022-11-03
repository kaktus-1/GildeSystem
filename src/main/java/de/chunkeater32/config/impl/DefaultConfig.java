package de.chunkeater32.config.impl;

import de.chunkeater32.config.Config;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

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
        cfg.addDefault("clan.name_length", 20);
        cfg.addDefault("clan.tag_length", 7);
        cfg.addDefault("clan.max_members", 20);
        cfg.options().copyDefaults(true);

        save();

        prefix = cfg.getString("prefix");
        clanNameLength = cfg.getInt("clan.name_length");
        clanTagLength = cfg.getInt("clan.tag_length");
        clanMaxMembers = cfg.getInt("clan.max_members");
    }
}
