package com.kiselev.enemy.service.profiler.model;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.vk.model.VKProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String firstName;

    private String lastName;

    private String fullName;

    private String sex;

    private String birthday;

    private Integer age;

    private String country;

    private String phone;

    private String email;

    private String city;

    private String address;

    private String telegram;

    private String instagram;

    private String vk;

    public Person(TelegramProfile profile) {
        String username = StringUtils.isNotEmpty(profile.userName()) ? profile.userName() : null;
        String phone = StringUtils.isNotEmpty(profile.phone()) ? profile.phone() : null;

        this.telegram = ObjectUtils.firstNonNull(username, phone);

        this.firstName = profile.firstName();
        this.lastName = profile.lastName();
        this.fullName = profile.firstName() + " " + profile.lastName();
        this.phone = phone;
    }

    public Person(InstagramProfile profile) {
        this.instagram = profile.username();

        this.fullName = profile.fullName();
        this.phone = profile.public_phone_number();
        this.email = profile.public_email();

        this.vk = profile.vk();
        this.telegram = profile.telegram();
    }

    public Person(VKProfile profile) {
        String phone = StringUtils.isNotEmpty(profile.phone()) ? profile.phone() : null;

        this.vk = profile.screenName();

        this.firstName = profile.firstName();
        this.lastName = profile.lastName();
        this.fullName = profile.fullName();
        this.sex = profile.sex().name();
        this.age = profile.age();
        this.birthday = profile.birthday();
        this.country = profile.country();
        this.city = profile.city();
        this.address = profile.country() + ", " + profile.city();
        this.phone = phone;

        this.instagram = profile.instagram();
        this.telegram = profile.telegram();
    }
}
