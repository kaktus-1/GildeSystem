package de.chunkeater32.utils;

import de.chunkeater32.config.impl.DefaultConfig;
import de.chunkeater32.cribeclan.CribeClan;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ChatUtils {

    private final static DefaultConfig config = ((DefaultConfig) CribeClan.getInstance()
            .getConfigRegistry()
            .getByClass(DefaultConfig.class));
    private final static FileConfiguration messages = config.getCfg();
    private final static String prefix = config.getPrefix();

    public static void sendMessage(CommandSender sender, String key, Replace... replaces) {
        sendMessage(sender, key, true, replaces);
    }

    public static void sendMessage(CommandSender sender, String key, boolean prefix, Replace... replaces) {
        if (!messages.isString(key)) {
            List<String> stringList = messages.getStringList(key);

            for (String s : stringList) {
                if (replaces != null)
                    for (Replace replace : replaces) {
                        s = s.replace(replace.getWhat(), replace.getTo());
                    }
                sendWithSplit(sender, s, prefix);
            }
            return;
        }

        String string = messages.getString(key);

        if (replaces != null)
            for (Replace replace : replaces) {
                string = string.replace(replace.getWhat(), replace.getTo());
            }

        sendWithSplit(sender, string, prefix);
    }

    private static void sendWithSplit(CommandSender sender, String string, boolean usePrefix) {
        for (String s : string.split("\n")) {
            if (usePrefix)
                sender.sendMessage(prefix + s);
            else
                sender.sendMessage(s);
        }
    }

}
