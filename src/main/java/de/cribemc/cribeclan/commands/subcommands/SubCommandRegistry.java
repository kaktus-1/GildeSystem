package de.cribemc.cribeclan.commands.subcommands;

import de.cribemc.cribeclan.config.Config;
import de.cribemc.cribeclan.config.ConfigRegistry;
import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.structure.NameableRegistry;
import lombok.SneakyThrows;
import org.reflections.Reflections;

public class SubCommandRegistry extends NameableRegistry<SubCommand> {

    @SneakyThrows
    public SubCommandRegistry() {
        Reflections reflections = new Reflections("de.chunkeater32.commands.subcommands.impl");
        ConfigRegistry configRegistry = CribeClan.getInstance().getConfigRegistry();
        Config byClass = configRegistry.getByClass(DefaultConfig.class);

        for (Class<? extends SubCommand> aClass : reflections.getSubTypesOf(SubCommand.class)) {
            SubCommand subCommand = aClass.getDeclaredConstructor().newInstance();
            register(subCommand);

            byClass.getCfg().addDefault("commands." + subCommand.getName() + ".description", subCommand.getDescription());
            subCommand.setDescription(byClass.getCfg().getString("commands." + subCommand.getName() + ".description"));
        }

        byClass.save();
    }
}
