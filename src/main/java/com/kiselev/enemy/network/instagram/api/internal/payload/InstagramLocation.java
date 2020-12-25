package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Location
 *
 * @author Yumaev
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramLocation {
    private double lat;
    private double lng;
    private String address;
    private String external_id;
    private String external_id_source;
    private String name;
    private String minimum_age;
}
