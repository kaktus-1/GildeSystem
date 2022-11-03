package de.chunkeater32.clan;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class User {

    private final UUID uuid;
    private final String name;
    @NonNull
    private Rank rank;
    private CustomRank customRank;

}
