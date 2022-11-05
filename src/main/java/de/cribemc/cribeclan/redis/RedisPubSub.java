package de.cribemc.cribeclan.redis;

import de.cribemc.cribeclan.clan.ClanRegistry;
import de.cribemc.cribeclan.structure.INameable;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class RedisPubSub implements INameable {

    @Getter
    private final String name = getClass().getSimpleName()
            .toLowerCase()
            .replace("pubsub", "");

    public abstract void onReceive(ClanRegistry clanRegistry, String[] args);

    public String prepareSend(String... args) {
        String collect = Arrays.stream(args).map(s -> s.replace("Ѿ", "")).collect(Collectors.joining("Ѿ"));
        return name + "Ѿ" + collect;
    }

}
