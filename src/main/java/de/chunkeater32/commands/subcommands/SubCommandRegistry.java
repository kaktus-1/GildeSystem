package de.chunkeater32.commands.subcommands;

import de.chunkeater32.config.Config;
import de.chunkeater32.config.ConfigRegistry;
import de.chunkeater32.config.impl.DefaultConfig;
import de.chunkeater32.cribeclan.CribeClan;
import de.chunkeater32.structure.NameableRegistry;
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
