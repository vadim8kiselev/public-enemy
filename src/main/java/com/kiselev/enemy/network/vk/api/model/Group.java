package com.kiselev.enemy.network.vk.api.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.network.vk.utils.BaseObjectDeserializer;
import com.kiselev.enemy.utils.flow.model.Id;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.groups.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.List;

@Data
@Accessors(fluent = true)
public class Group implements Id {

    private static final String GROUP_URL_TEMPLATE = "[%s](https://vk.com/club%s)";

    @SerializedName("id")
    private String id;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("name")
    private String title;

    @SerializedName("country")
    @JsonAdapter(BaseObjectDeserializer.class)
    private String country;

    @SerializedName("city")
    @JsonAdapter(BaseObjectDeserializer.class)
    private String city;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("links")
    private List<LinksItem> links;

    @SerializedName("contacts")
    private List<ContactsItem> contacts;

    @SerializedName("site")
    private String site;

    @SerializedName("addresses")
    private AddressesInfo addresses;

    @SerializedName("verified")
    private BoolInt verified;

    @SerializedName("is_closed")
    private GroupIsClosed isClosed;

    @SerializedName("deactivated")
    private String deactivated;

    @SerializedName("photo_100")
    private URL photo100;

    @SerializedName("photo_200")
    private URL photo200;

    @SerializedName("photo_50")
    private URL photo50;

    @SerializedName("wiki_page")
    private String wikiPage;

    @SerializedName("members_count")
    private Integer membersCount;

    @SerializedName("cover")
    private Cover cover;

    @SerializedName("main_album_id")
    private Integer mainAlbumId;

    @SerializedName("counters")
    private CountersGroup counters;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return String.format(
                GROUP_URL_TEMPLATE,
                title,
                id
        );
    }
}
