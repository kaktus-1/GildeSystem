package de.chunkeater32.config;

import de.chunkeater32.cribeclan.CribeClan;
import de.chunkeater32.structure.INameable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class Config implements INameable {

    private String name;
    private File file;
    private FileConfiguration cfg;

    public Config(String name) {
        this.name = name;
        this.file = new File(CribeClan.getInstance().getDataFolder(), name + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        this.file = new File(CribeClan.getInstance().getDataFolder(), name + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStart() {

    }

}