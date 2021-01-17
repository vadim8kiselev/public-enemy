package com.kiselev.enemy.service.profiler.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kiselev.enemy.network.vk.utils.MapperHolder;
import com.kiselev.enemy.service.profiler.model.Conversation;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfilingUtils {

    public static String identifier(SocialNetwork socialNetwork, String request) {
        if (request == null) {
            return null;
        }
        Pattern pattern = socialNetwork.pattern();
        Matcher matcher = pattern.matcher(request);

        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }

    public static LocalDateTime timestamp(Integer timestamp) {
        return timestamp(
                Long.valueOf(timestamp)
        );
    }

    public static LocalDateTime timestamp(Long timestamp) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static LocalDateTime timestampSeconds(Integer timestamp) {
        return timestampSeconds(
                Long.valueOf(timestamp)
        );
    }

    public static LocalDateTime timestampSeconds(Long timestamp) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    @SneakyThrows
    public static String json(Object object) {
        return MapperHolder.OBJECT_MAPPER
                .writeValueAsString(object);
    }

    @SneakyThrows
    public static void file(String folder, String name, String body) {
        FileUtils.writeStringToFile(
                new File(folder + "/" + name + ".json"),
                body,
                Charset.defaultCharset());
    }

    @SneakyThrows
    public static void cache(String name, Object object) {
        String json = json(object);

        FileUtils.writeStringToFile(
                new File("cache/" + name+ ".json"),
                json,
                Charset.defaultCharset());
    }

    @SneakyThrows
    public static List<Conversation> cache(String name) {
        return MapperHolder.OBJECT_MAPPER.readValue(
                new File("cache/" + name+ ".json"),
                new TypeReference<List<Conversation>>() {}
        );
    }
}
