package com.kiselev.enemy.network.telegram.api.bot.internal;

import com.google.gson.Gson;
import com.kiselev.enemy.network.vk.utils.MapperHolder;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TelegramBotClient {

    private static final String API_URL = "https://api.telegram.org/bot";

    private static final String API_FILE_URL = "https://api.telegram.org/file/bot";

    private static final Integer TIMEOUT = 0;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(75, TimeUnit.SECONDS)
            .writeTimeout(75, TimeUnit.SECONDS)
            .readTimeout(75, TimeUnit.SECONDS)
            .build();

    private OkHttpClient clientWithTimeout = client;

    private Gson gson = MapperHolder.GSON;

    @Value("${com.kiselev.enemy.telegram.bot.token}")
    private String token;

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void send(final T request, final Callback<T, R> callback) {
        OkHttpClient client = client(request.getTimeoutSeconds());
        client.newCall(post(request)).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                R result = null;
                Exception exception = null;
                try {
                    result = gson.fromJson(response.body().string(), request.getResponseType());
                } catch (Exception e) {
                    exception = e;
                }
                if (result != null) {
                    callback.onResponse(request, result);
                } else if (exception != null) {
                    IOException ioEx = exception instanceof IOException ? (IOException) exception : new IOException(exception);
                    callback.onFailure(request, ioEx);
                } else {
                    callback.onFailure(request, new IOException("Empty response"));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }
        });
    }

    @SneakyThrows
    public byte[] download(final String file) {
        return IOUtils.toByteArray(
                new URL(API_FILE_URL + token + "/" + file)
        );
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> R send(final BaseRequest<T, R> request) {
        try {
            OkHttpClient client = client(request.getTimeoutSeconds());
            Response response = client.newCall(post(request)).execute();
            return gson.fromJson(response.body().string(), request.getResponseType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OkHttpClient client(Integer timeout) {
        int timeoutMillis = timeout * 1000;

        if (client.readTimeoutMillis() == 0 || client.readTimeoutMillis() > timeoutMillis) return client;
        if (clientWithTimeout.readTimeoutMillis() > timeoutMillis) return clientWithTimeout;

        clientWithTimeout = client.newBuilder().readTimeout(timeoutMillis + 1000, TimeUnit.MILLISECONDS).build();
        return clientWithTimeout;
    }

    private Request post(BaseRequest<?, ?> request) {
        return new Request.Builder()
                .url(API_URL + token + "/" + request.getMethod())
                .post(createRequestBody(request))
                .build();
    }

    private RequestBody createRequestBody(BaseRequest<?, ?> request) {
        if (request.isMultipart()) {
            MediaType contentType = MediaType.parse(request.getContentType());

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                String name = parameter.getKey();
                Object value = parameter.getValue();
                if (value instanceof byte[]) {
                    builder.addFormDataPart(name, request.getFileName(), RequestBody.create(contentType, (byte[]) value));
                } else if (value instanceof File) {
                    builder.addFormDataPart(name, request.getFileName(), RequestBody.create(contentType, (File) value));
                } else {
                    builder.addFormDataPart(name, toParamValue(value));
                }
            }

            return builder.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                builder.add(parameter.getKey(), toParamValue(parameter.getValue()));
            }
            return builder.build();
        }
    }

    private String toParamValue(Object obj) {
        if (obj != null && (obj.getClass().isPrimitive() ||
                obj.getClass().isEnum() ||
                obj.getClass().getName().startsWith("java.lang"))) {
            return String.valueOf(obj);
        }
        return gson.toJson(obj);
    }
}
