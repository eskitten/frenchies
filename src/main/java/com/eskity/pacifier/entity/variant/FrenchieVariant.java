package com.eskity.pacifier.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum FrenchieVariant {
    DEFAULT(0),
    LIGHTGRAY(1);

    private static final FrenchieVariant[] BY_ID = Arrays.stream(values())
            .sorted(Comparator.comparingInt(FrenchieVariant::getId)).toArray(FrenchieVariant[]::new);
    private final int id;
    FrenchieVariant(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    public static FrenchieVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
