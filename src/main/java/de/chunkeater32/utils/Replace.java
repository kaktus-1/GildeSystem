package de.chunkeater32.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Replace {

    private final String what;
    private final String to;

    public static Replace of(String what, String to) {
        return new Replace("%" + what + "%", to);
    }

}
