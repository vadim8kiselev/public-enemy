package com.kiselev.enemy.network.instagram.api.internal.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Kohonovsky
 * @since 2019-04-28
 */
@RequiredArgsConstructor
public enum InstagramUserGenderEnum {

    MALE("1"),
    FEMALE("2"),
    NOT_SPECIFIED("3");

    private static Map<String, InstagramUserGenderEnum> gendersMap = new HashMap<>(3);

    static {
        gendersMap.put(MALE.getInstagramCode(), MALE);
        gendersMap.put(FEMALE.getInstagramCode(), FEMALE);
        gendersMap.put(NOT_SPECIFIED.getInstagramCode(), NOT_SPECIFIED);
    }

    @Getter
    private final String instagramCode;

    /**
     * @param value - instagram gender code or enum value
     * @return enum
     */
    @JsonCreator
    public static InstagramUserGenderEnum forValue(String value) {
        InstagramUserGenderEnum resultEnum = gendersMap.get(StringUtils.lowerCase(value));
        if (resultEnum == null) {
            resultEnum = InstagramUserGenderEnum.valueOf(value);
        }
        return resultEnum;
    }

}


