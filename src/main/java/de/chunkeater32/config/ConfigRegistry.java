package de.chunkeater32.config;


import de.chunkeater32.config.impl.DefaultConfig;
import de.chunkeater32.structure.NameableRegistry;

public class ConfigRegistry extends NameableRegistry<Config> {

    public ConfigRegistry() {
        register(new DefaultConfig());

        getObjects().forEach(Config::onStart);
    }

    public void reload() {
        getObjects().forEach(Config::reload);
    }

}