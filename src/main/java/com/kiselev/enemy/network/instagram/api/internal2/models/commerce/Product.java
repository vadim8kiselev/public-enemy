package com.kiselev.enemy.network.instagram.api.internal2.models.commerce;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;

import lombok.Data;

@Data
public class Product extends IGBaseModel {
    private String name;
    private String price;
    private String current_price;
    private String full_price;
    private long product_id;
    private Profile merchant;
    private String compound_product_id;
    private String description;
    private String retailer_id;
    private String external_url;

}
