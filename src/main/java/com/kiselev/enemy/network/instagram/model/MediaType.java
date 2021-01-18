package com.kiselev.enemy.network.instagram.model;

public enum MediaType {

    PHOTO(1),
    VIDEO(2),
    ALBUM(8);

    private Integer type;

    MediaType(Integer type) {
        this.type = type;
    }

    public static MediaType by(Integer type) {
        for (MediaType mediaType : values()) {
            if (mediaType.type.equals(type)) {
                return mediaType;
            }
        }
        throw new RuntimeException(String.format("Unknown type of post %s", type));
    }
}
