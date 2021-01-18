package com.kiselev.enemy.network.instagram.api.internal2.responses.commerce;

import com.kiselev.enemy.network.instagram.api.internal2.models.commerce.Product;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

@Data
public class CommerceProductsDetailsResponse extends IGResponse {
    private Profile merchant;
    private Product product_item;
}
