package de.chunkeater32.utils;

import de.chunkeater32.config.impl.DefaultConfig;
import de.chunkeater32.config.impl.MessagesConfig;
import de.chunkeater32.cribeclan.CribeClan;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ChatUtils {

    private final static DefaultConfig config = ((DefaultConfig) CribeClan.getInstance()
            .getConfigRegistry()
            .getByClass(DefaultConfig.class));
    private final static FileConfiguration messages = CribeClan.getInstance()
            .getConfigRegistry()
            .getByClass(MessagesConfig.class)
            .getCfg();
    private final static String prefix = config.getPrefix();

    public static void sendMessage(CommandSender sender, String key, Replace... replaces) {
        if (!messages.isString(key)) {
            List<String> stringList = messages.getStringList(key);

            for (String s : stringList) {
                if (replaces != null)
                    for (Replace replace : replaces) {
                        s = s.replace(replace.getWhat(), replace.getTo());
                    }
                sender.sendMessage(prefix + s);
            }
            return;
        }

        String string = messages.getString(key);

        if (replaces != null)
            for (Replace replace : replaces) {
                string = string.replace(replace.getWhat(), replace.getTo());
            }

        sender.sendMessage(prefix + string);
    }

}
