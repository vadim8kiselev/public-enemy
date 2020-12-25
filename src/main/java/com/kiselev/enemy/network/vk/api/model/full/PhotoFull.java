package com.kiselev.enemy.network.vk.api.model.full;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.api.model.Photo;
import com.vk.api.sdk.objects.base.PropertyExists;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class PhotoFull extends Photo {

    @SerializedName("access_key")
    private String accessKey;

    /**
     * Returns if the photo is hidden above the wall
     */
    @SerializedName("hidden")
    private PropertyExists hidden;

    /**
     * URL of image with 1280 px width
     */
    @SerializedName("photo_1280")
    private URL photo1280;

    /**
     * URL of image with 130 px width
     */
    @SerializedName("photo_130")
    private URL photo130;

    /**
     * URL of image with 2560 px width
     */
    @SerializedName("photo_2560")
    private URL photo2560;

    /**
     * URL of image with 604 px width
     */
    @SerializedName("photo_604")
    private URL photo604;

    /**
     * URL of image with 75 px width
     */
    @SerializedName("photo_75")
    private URL photo75;

    /**
     * URL of image with 807 px width
     */
    @SerializedName("photo_807")
    private URL photo807;

    /**
     * Post ID
     */
    @SerializedName("post_id")
    private Integer postId;

    /**
     * Real position of the photo
     */
    @SerializedName("real_offset")
    private Integer realOffset;

    @SerializedName("sizes")
    private List<PhotoSizes> sizes;
}
