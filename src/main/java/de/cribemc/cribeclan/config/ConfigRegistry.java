package de.cribemc.cribeclan.config;


import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.structure.NameableRegistry;

public class ConfigRegistry extends NameableRegistry<Config> {

    public ConfigRegistry() {
        register(new DefaultConfig());

        getObjects().forEach(Config::onStart);
    }

    public void reload() {
        getObjects().forEach(Config::reload);
    }

}