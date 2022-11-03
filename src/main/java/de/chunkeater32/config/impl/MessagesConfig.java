package de.chunkeater32.config.impl;

import de.chunkeater32.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class MessagesConfig extends Config {
    public MessagesConfig() {
        super("messages");
    }

    @Override
    public void onStart() {
        FileConfiguration cfg = getCfg();

        cfg.addDefault("syntax", Arrays.asList("§e%command%",
                "   §7=> §7%description%",
                "   §7=> §e%syntax%"));
        cfg.addDefault("not-found", "§7Der Unterbefehl §e%command% §7existiert nicht.");
        cfg.addDefault("no-perm", "§7Keine Berechtigung!");
        cfg.addDefault("not-allowed", Arrays.asList("§7Deine Eingabe enthält unerlaubte Zeichen!",
                "§7Erlaubte Zeichen: §bA-Z§8, §bÄ§8, §bÜ§8, §bÖ§8, §b0-9"));
        cfg.addDefault("not-member", "§7Du bist aktuell nicht Mitglied eines Clans!");
        cfg.addDefault("already-member", "§7Du bist bereits Mitglied eines Clans!");
        cfg.addDefault("clan-tag-too-long", "§7Der Clan Tag darf maximal 7 Zeichen lang sein.");
        cfg.addDefault("clan-name-too-long", "§7Der Clan Name darf maximal 20 Zeichen lang sein.");
        cfg.options().copyDefaults(true);

        save();
    }
}
