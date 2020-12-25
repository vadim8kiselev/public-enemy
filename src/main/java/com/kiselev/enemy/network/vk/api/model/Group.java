package com.kiselev.enemy.network.vk.api.model;

import com.google.gson.annotations.SerializedName;
import com.kiselev.enemy.utils.flow.model.Id;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.groups.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.List;

@Data
@Accessors(fluent = true)
public class Group implements Id {

    private static final String GROUP_URL_TEMPLATE = "https://vk\\.com/club%s";

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("deactivated")
    private String deactivated;

    @SerializedName("is_closed")
    private GroupIsClosed isClosed;

    @SerializedName("photo_100")
    private URL photo100;

    @SerializedName("photo_200")
    private URL photo200;

    @SerializedName("photo_50")
    private URL photo50;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("city")
    private BaseObject city;

    @SerializedName("country")
    private Country country;

    @SerializedName("verified")
    private BoolInt verified;

    @SerializedName("description")
    private String description;

    @SerializedName("wiki_page")
    private String wikiPage;

    @SerializedName("members_count")
    private Integer membersCount;

    @SerializedName("counters")
    private CountersGroup counters;

    @SerializedName("cover")
    private Cover cover;

    @SerializedName("status")
    private String status;

    @SerializedName("main_album_id")
    private Integer mainAlbumId;

    @SerializedName("links")
    private List<LinksItem> links;

    @SerializedName("contacts")
    private List<ContactsItem> contacts;

    @SerializedName("site")
    private String site;

    @SerializedName("addresses")
    private AddressesInfo addresses;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return String.format(
                GROUP_URL_TEMPLATE,
                id
        );
    }
}
