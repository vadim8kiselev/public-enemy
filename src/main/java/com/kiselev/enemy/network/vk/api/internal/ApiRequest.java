package com.kiselev.enemy.network.vk.api.internal;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.kiselev.enemy.network.vk.utils.GsonHolder;
import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiServerException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.exceptions.ExceptionMapper;
import com.vk.api.sdk.objects.base.Error;
import com.vk.api.sdk.queries.EnumParam;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public abstract class ApiRequest<R, T> {

    private static final Logger LOG = LogManager.getLogger(ApiRequest.class);

    private static final String API_VERSION = "5.101";

    private static final String API_ADDRESS = "https://api.vk.com/method/";

    private static final String OAUTH_ENDPOINT = "https://oauth.vk.com/";

    private static final int DEFAULT_RETRY_ATTEMPTS_INTERNAL_SERVER_ERROR_COUNT = 3;

    private final TransportClient transportClient;

    private final Gson gson;

    private final String url;

    private final int retryAttempts;

    private final Map<String, String> params;

    private final String method;

    private final Type responseClass;

    public ApiRequest(String token, String method, Type type) {
        this.transportClient = HttpTransportClient.getInstance();
        this.url = API_ADDRESS + method;
        this.gson = GsonHolder.GSON;
        this.retryAttempts = DEFAULT_RETRY_ATTEMPTS_INTERNAL_SERVER_ERROR_COUNT;
        this.params = new HashMap<>();
        this.method = method;
        this.responseClass = type;
        version(API_VERSION);
        accessToken(token);
    }

    public T execute() throws ApiException, ClientException {
        ApiServerException exception = null;
        for (int i = 0; i < retryAttempts; i++) {
            try {
                return executeWithoutRetry();
            } catch (ApiServerException e) {
                LOG.warn("API Server error", e);
                exception = e;
            }
        }

        throw exception;
    }

    private T executeWithoutRetry() throws ClientException, ApiException {
        String textResponse = executeAsString();
        JsonReader jsonReader = new JsonReader(new StringReader(textResponse));
        JsonObject json = (JsonObject) new JsonParser().parse(jsonReader);

        if (json.has("error")) {
            JsonElement errorElement = json.get("error");
            Error error;
            try {
                error = gson.fromJson(errorElement, Error.class);
            } catch (JsonSyntaxException e) {
                LOG.error("Invalid JSON: " + textResponse, e);
                throw new ClientException("Can't parse json response");
            }

            ApiException exception = ExceptionMapper.parseException(error);

            LOG.error("API error", exception);
            throw exception;
        }

        JsonElement response = json;
        if (json.has("response")) {
            response = json.get("response");
        }

        try {
            return gson.fromJson(response, responseClass);
        } catch (JsonSyntaxException e) {
            LOG.error("Invalid JSON: " + textResponse, e);
            throw new ClientException("Can't parse json response");
        }
    }

    public String executeAsString() throws ClientException {
        ClientResponse response;
        try {
            response = transportClient.post(url, getBody());
        } catch (IOException e) {
            LOG.error("Problems with request: " + url, e);
            throw new ClientException("I/O exception");
        }

        if (response.getStatusCode() != 200) {
            throw new ClientException("Internal API server error. Wrong status code: " + response.getStatusCode() + ". Content: " + response.getContent());
        }

        Map<String, String> headers = response.getHeaders();

        if (!headers.containsKey("Content-Type")) {
            throw new ClientException("No content type header");
        }

        String contentType = headers.get("Content-Type");

        if (!contentType.contains("application/json") && !contentType.contains("text/javascript")) {
            throw new ClientException("Invalid content type");
        }

        return response.getContent();
    }

    public ClientResponse executeAsRaw() throws ClientException {
        try {
            return transportClient.post(url, getBody());
        } catch (IOException e) {
            LOG.error("Problems with request: " + url, e);
            throw new ClientException("I/O exception");
        }
    }







    /**
     * Convert boolean value to integer flag
     *
     * @param param value
     * @return integer flag
     */
    private static String boolAsParam(boolean param) {
        return param ? "1" : "0";
    }

    /**
     * Build request parameter map to query
     *
     * @param params parameters
     * @return string query
     */
    private static String mapToGetString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + (entry.getValue() != null ? escape(entry.getValue()) : ""))
                .collect(Collectors.joining("&"));
    }

    /**
     * Encode request data
     *
     * @param data request data
     * @return encoded data
     */
    private static String escape(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Set access token
     *
     * @param value access token
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    protected R accessToken(String value) {
        return unsafeParam("access_token", value);
    }

    /**
     * Set client secret
     *
     * @param value client secret
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    protected R clientSecret(String value) {
        return unsafeParam("client_secret", value);
    }

    /**
     * Set lang
     *
     * @param value lang
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R lang(Lang value) {
        return unsafeParam("lang", value.getValue());
    }

    /**
     * Set version
     *
     * @param value version
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    protected R version(String value) {
        return unsafeParam("v", value);
    }


    /**
     * Set captcha sid
     *
     * @param value captcha sid
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R captchaSid(String value) {
        return unsafeParam("captcha_sid", value);
    }

    /**
     * Set captcha key
     *
     * @param value captcha key
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R captchaKey(String value) {
        return unsafeParam("captcha_key", value);
    }

    /**
     * Set confirmation
     *
     * @param value confirm
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R confirm(Boolean value) {
        return unsafeParam("confirm", value);
    }


    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, String value) {
        params.put(key, value);
        return getThis();
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, int value) {
        return unsafeParam(key, Integer.toString(value));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, boolean value) {
        return unsafeParam(key, boolAsParam(value));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, Collection<?> value) {
        return unsafeParam(key, value.stream().map(Objects::toString).collect(Collectors.joining(",")));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public <U> R unsafeParam(String key, U... value) {
        return unsafeParam(key, asList(value));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, int[] value) {
        return unsafeParam(key, IntStream.of(value).mapToObj(Integer::toString).collect(Collectors.joining(",")));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, double value) {
        return unsafeParam(key, Double.toString(value));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, float value) {
        return unsafeParam(key, Float.toString(value));
    }

    /**
     * Set parameter
     *
     * @param key   name of parameter
     * @param value value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, EnumParam value) {
        return unsafeParam(key, value.getValue());
    }

    /**
     * Set parameter
     *
     * @param key    name of parameter
     * @param fields value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, EnumParam... fields) {
        return unsafeParam(key, Arrays.stream(fields).map(EnumParam::getValue).collect(Collectors.joining(",")));
    }

    /**
     * Set parameter
     *
     * @param key    name of parameter
     * @param fields value of parameter
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public R unsafeParam(String key, List<? extends EnumParam> fields) {
        return unsafeParam(key, fields.stream().map(EnumParam::getValue).collect(Collectors.joining(",")));
    }

    protected String getBody() {
        return mapToGetString(build());
    }

    /**
     * Get reference to this object
     *
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    protected abstract R getThis();

    /**
     * Get list of required parameter names
     *
     * @return list of names
     */
    protected abstract Collection<String> essentialKeys();

    /**
     * Get map of parameter values
     *
     * @return map of values
     */
    public Map<String, String> build() {
        if (!params.keySet().containsAll(essentialKeys())) {
            throw new IllegalArgumentException("Not all the keys are passed: essential keys are " + essentialKeys());
        }

        return Collections.unmodifiableMap(params);
    }

    /**
     * Get method name
     *
     * @return method name
     */
    public String getMethod() {
        return method;
    }
}

