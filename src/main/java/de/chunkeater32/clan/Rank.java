package de.chunkeater32.clan;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rank {

    OWNER("§cOwner"), MODERATOR("§aModerator"), MEMBER("§7Mitglied");

    private final String prefix;

}
