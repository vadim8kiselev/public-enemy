package com.kiselev.enemy.network.instagram.api.internal;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramLoginPayload;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramLoginResult;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramLoginTwoFactorPayload;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramLoginRequest;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramLoginTwoFactorRequest;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramRequest;
import com.kiselev.enemy.network.instagram.api.internal.util.InstagramGenericUtil;
import com.kiselev.enemy.network.instagram.api.internal.util.InstagramHashUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

@Log4j
public class Instagram4j implements Serializable {

    @Getter
    private String deviceId;

    @Getter
    private String uuid;

    @Getter
    private String advertisingId;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    private CredentialsProvider credentialsProvider;

    @Getter
    @Setter
    private HttpHost proxy;

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private String rankToken;

    @Getter
    private boolean isLoggedIn;

    @Getter
    private HttpResponse lastResponse;

    @Getter
    @Setter
    private boolean debug;

    @Getter
    private CookieStore cookieStore;

    private CloseableHttpClient client;

    @Getter
    @Setter
    private String AUTH_VALUE = "0";

    @Getter
    @Setter
    private String X_MID = "0";

    @Getter
    @Setter
    private String WWW_CLAIM = "0";

    private String identifier;

    /**
     * @param username Username
     * @param password Password
     */
    public Instagram4j(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    /**
     * @param username            Username
     * @param password            Password
     * @param userId              UserId
     * @param uuid                UUID
     * @param cookieStore         Cookie Store
     * @param proxy               proxy
     * @param credentialsProvider proxy credential
     */
    @Builder
    public Instagram4j(String username, String password, long userId, String uuid, CookieStore cookieStore,
                       HttpHost proxy, CredentialsProvider credentialsProvider) {
        super();
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.uuid = uuid;
        this.cookieStore = cookieStore;
        this.proxy = proxy;
        this.credentialsProvider = credentialsProvider;
        this.isLoggedIn = true;
    }

    /**
     * Setup some variables
     */
    public void setup() {
        log.info("Setup...");

        if (StringUtils.isEmpty(this.username)) {
            throw new IllegalArgumentException("Username is mandatory.");
        }

        if (StringUtils.isEmpty(this.password)) {
            throw new IllegalArgumentException("Password is mandatory.");
        }

        this.deviceId = InstagramHashUtil.generateDeviceId(this.username, this.password);

        if (StringUtils.isEmpty(this.uuid)) {
            this.uuid = InstagramGenericUtil.generateUuid(true);
        }

        if (StringUtils.isEmpty(this.advertisingId)) {
            this.advertisingId = InstagramGenericUtil.generateUuid(true);
        }

        if (this.cookieStore == null) {
            this.cookieStore = new BasicCookieStore();
        }

        log.info("Device ID is: " + this.deviceId + ", random id: " + this.uuid);
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (proxy != null) {
            builder.setProxy(proxy);
        }

        if (credentialsProvider != null)
            builder.setDefaultCredentialsProvider(credentialsProvider);

        builder.setDefaultCookieStore(this.cookieStore);
        this.client = builder.build();
    }

    /**
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public InstagramLoginResult login() throws ClientProtocolException, IOException {

        log.info("Logging with user " + username + " and password " + password.replaceAll("[a-zA-Z0-9]", "*"));

        InstagramLoginPayload loginRequest = InstagramLoginPayload.builder()
                .username(username).password(password)
                .guid(uuid).device_id(deviceId).phone_id(InstagramGenericUtil.generateUuid(true))
                .login_attempt_account(0)._csrftoken("missing").build();

        InstagramLoginRequest req = new InstagramLoginRequest(loginRequest);
        InstagramLoginResult loginResult = this.sendRequest(req);

        if (loginResult.getStatus().equals("ok")) {
            this.isLoggedIn = true;
        }
        if (loginResult.getTwo_factor_info() != null) {
            identifier = loginResult.getTwo_factor_info().getTwo_factor_identifier();
        } else if (loginResult.getChallenge() != null) {
            // logic for challenge
            log.info("Challenge required: " + loginResult.getChallenge());
        }

        return loginResult;
    }

    public InstagramLoginResult login(String verificationCode) throws ClientProtocolException, IOException {
        if (identifier == null) {
            login();
        }
        InstagramLoginTwoFactorPayload loginRequest = InstagramLoginTwoFactorPayload.builder().username(username)
                .verification_code(verificationCode).two_factor_identifier(identifier).password(password).guid(uuid)
                .device_id(deviceId).phone_id(InstagramGenericUtil.generateUuid(true)).login_attempt_account(0)
                ._csrftoken(getOrFetchCsrf()).build();
        InstagramLoginTwoFactorRequest req = new InstagramLoginTwoFactorRequest(loginRequest);
        InstagramLoginResult loginResult = this.sendRequest(req);

        if (loginResult.getStatus().equals("ok")) {
            this.isLoggedIn = true;
        }
        return loginResult;
    }

    /**
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String getOrFetchCsrf() throws ClientProtocolException, IOException {
        Optional<Cookie> checkCookie = getCsrfCookie();
        if (!checkCookie.isPresent()) {
            return "0";
        }
        String csrfToken = checkCookie.get().getValue();
        return csrfToken;
    }

    public Optional<Cookie> getCsrfCookie() {
        return cookieStore.getCookies().stream().filter(cookie -> cookie.getName().equalsIgnoreCase("csrftoken"))
                .findFirst();
    }

    /**
     * Send request to endpoint
     *
     * @param request Request object
     * @return success flag
     * @throws IOException
     * @throws ClientProtocolException
     */
    public <T> T sendRequest(InstagramRequest<T> request) throws ClientProtocolException, IOException {

        log.info("Sending request: " + request.getClass().getName());

        if (!this.isLoggedIn && request.requiresLogin()) {
            throw new IllegalStateException("Need to login first!");
        }

        // wait to simulate real human interaction
        randomWait();

        request.setApi(this);
        T response = request.execute();
        this.setHeaders();

        log.debug("Result for " + request.getClass().getName() + ": " + response);

        return response;
    }

    @SneakyThrows
    public HttpResponse executeHttpRequest(HttpUriRequest req) {
        lastResponse = client.execute(req);
        return lastResponse;
    }

    private void setHeaders() {
        InstagramGenericUtil.getFirstHeaderValue(getLastResponse(), "ig-set-authorization")
                .ifPresent(this::setAUTH_VALUE);
        InstagramGenericUtil.getFirstHeaderValue(getLastResponse(), "x-ig-set-www-claim").ifPresent(this::setWWW_CLAIM);
        InstagramGenericUtil.getFirstHeaderValue(getLastResponse(), "ig-set-x-mid").ifPresent(this::setX_MID);
    }

    @SneakyThrows
    private void randomWait() {
        long wait = 300 + (long) (new Random().nextDouble() * (double) (400 - 300));
        Thread.sleep(wait);
    }
}
