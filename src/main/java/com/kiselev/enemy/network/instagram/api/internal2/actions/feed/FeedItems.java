package com.kiselev.enemy.network.instagram.api.internal2.actions.feed;

import java.util.Collection;
import java.util.stream.Stream;

public class FeedItems {
    public static <T> Stream<T> filter(Collection<? super T> feed_items, Class<T> clazz) {
        return feed_items.stream().filter(clazz::isInstance).map(clazz::cast);
    }
}
