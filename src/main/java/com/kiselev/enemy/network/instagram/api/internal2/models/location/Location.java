package com.kiselev.enemy.network.instagram.api.internal2.models.location;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(Include.NON_NULL)
public class Location extends IGBaseModel {
    private Long pk;
    private String external_id;
    private String short_name;
    private String name;
    private String external_source;
    private Double lat;
    private Double lng;
    private String address;
    private String city;
    private Integer minimum_age;

    @Data
    @ToString(callSuper = true)
    public static class Venue extends Location {
        @JsonAlias("external_id_source")
        private String external_source;
    }
}
