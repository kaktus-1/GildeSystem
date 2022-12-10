package de.cribemc.cribeclan.redis;

import de.cribemc.cribeclan.CribeClan;
import de.cribemc.cribeclan.commands.GildeCommand;
import de.cribemc.cribeclan.config.Config;
import de.cribemc.cribeclan.config.impl.DefaultConfig;
import de.cribemc.cribeclan.structure.NameableRegistry;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.reflections.Reflections;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

public class RedisRegistry extends NameableRegistry<RedisPubSub> {

    private final CribeClan instance = CribeClan.getInstance();
    private final Logger logger = instance.getLogger();
    private final JedisPool pool;

    @SneakyThrows
    public RedisRegistry() {
        logger.info("Trying to connect to redis...");

        Config config = instance.getConfigRegistry().getByClass(DefaultConfig.class);
        FileConfiguration cfg = config.getCfg();

        pool = new JedisPool(cfg.getString("redis.host"),
                cfg.getInt("redis.port"),
                cfg.getString("redis.user"),
                cfg.getString("redis.password"));

        Reflections reflections = new Reflections("de.cribemc.cribeclan.redis.pub");
        for (Class<? extends RedisPubSub> aClass : reflections.getSubTypesOf(RedisPubSub.class)) {
            register(aClass.getDeclaredConstructor().newInstance());
        }

        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            try (Jedis jedis = pool.getResource()) {
                logger.info("[REDIS] Listening now to all clan events...");

                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        String[] s = message.split("Ѿ");

                        RedisPubSub byName = getByName(s[0]);

                        for (UUID uuid : GildeCommand.debug) {
                            Player player = Bukkit.getPlayer(uuid);
                            if (player != null) {
                                player.sendMessage("§a[CLAN] §7in:");
                                for (String s1 : s) {
                                    player.sendMessage("       §c" + s1);
                                }
                            }
                        }

                        byName.onReceive(instance.getClanRegistry(), Arrays.copyOfRange(s, 1, s.length));
                    }
                }, "clan");
            }
        });
    }

    public void send(Class<? extends RedisPubSub> sub, String... args) {
        String s = getByClass(sub).prepareSend(args);

        String[] s1 = s.split("Ѿ");
        for (UUID uuid : GildeCommand.debug) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage("§a[CLAN] §7out:");
                for (String s2 : s1) {
                    player.sendMessage("       §c" + s2);
                }
            }
        }

        try (Jedis ressource = pool.getResource()) {
            ressource.publish("clan", s);
        }
    }

    public void destroy() {
        if (pool != null)
            pool.close();
    }
}
