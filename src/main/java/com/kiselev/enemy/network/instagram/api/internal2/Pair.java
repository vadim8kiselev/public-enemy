package com.kiselev.enemy.network.instagram.api.internal2;

import lombok.Data;

@Data
public class Pair<A, B> {

    private A first;

    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
