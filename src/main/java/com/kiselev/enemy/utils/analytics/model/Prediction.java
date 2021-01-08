package com.kiselev.enemy.utils.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
public class Prediction<Type> {

    private Type value;

    private Long statistics;
}
