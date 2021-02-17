package com.kiselev.enemy.utils.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
public class Prediction<Type> {

    public static final String MESSAGE = "%s (%s%%)";

    private Type value;

    private Long statistics;

    public boolean sufficient(long statistics) {
        return this.statistics >= statistics;
    }

    public String message() {
        return String.format(MESSAGE, value, statistics);
    }
}
