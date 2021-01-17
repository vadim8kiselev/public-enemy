package com.kiselev.enemy.network.instagram.api.internal2;

import com.kiselev.enemy.network.instagram.api.internal2.actions.IGClientActions;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGLoginException;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGResponseException.IGFailedResponse;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsLoginRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsTwoFactorLoginRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.qe.QeSyncRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.LoginResponse;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGChallengeUtils;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;
import com.kiselev.enemy.utils.asker.console.Reader;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Data
@Slf4j
public class IGClient {

    private static final String OK = "ok";

    private final String username;
    private final String password;

    private String guid;
    private String phoneId;
    private String deviceId;

    private transient OkHttpClient httpClient;
    private IGDevice device;

    private transient String sessionId;
    private transient IGClientActions actions;

    private transient String encryptionId, encryptionKey, authorization;
    private boolean loggedIn = false;
    private Profile selfProfile;

    private boolean available;
    private LocalDateTime isNotAvailableFrom;

    public IGClient(String username, String password) {
        this.username = username;
        this.password = password;
        this.guid = IGUtils.randomUuid();
        this.phoneId = IGUtils.randomUuid();
        this.deviceId = IGUtils.generateDeviceId(username, password);
        this.httpClient = IGUtils.defaultHttpClientBuilder().build();
        this.device = IGAndroidDevice.GOOD_DEVICES[new Random().nextInt(IGAndroidDevice.GOOD_DEVICES.length)];
        this.sessionId = IGUtils.randomUuid();
        this.actions = new IGClientActions(this);
        this.available = true;
    }

    @SneakyThrows
    public static synchronized IGClient authenticate(String username, String password) {
        IGClient client = new IGClient(username, password);

        LoginResponse response = client.sendLoginRequest()
                .exceptionally(tr -> {
                    LoginResponse loginResponse = IGFailedResponse.of(tr.getCause(), LoginResponse.class);
                    if (loginResponse.getTwo_factor_info() != null) {
                        loginResponse = client.authenticateWithTwoFactors(client, loginResponse);
                    }
                    if (loginResponse.getChallenge() != null) {
                        loginResponse = client.authenticateWithChallenge(client, loginResponse);
                        client.setLoggedInState(loginResponse);
                    }
                    return loginResponse;
                })
                .join();

        if (!client.isLoggedIn()) {
            throw new IGLoginException(client, response);
        }

        return client;
    }

    private LoginResponse authenticateWithTwoFactors(IGClient client, LoginResponse response) {
        return IGChallengeUtils.resolveTwoFactor(
                client,
                response,
                readCode("Input two factors security code: "));
    }

    private LoginResponse authenticateWithChallenge(IGClient client, LoginResponse response) {
        return IGChallengeUtils.resolveChallenge(
                client,
                response,
                readCode("Input challenge security code: "));
    }

    private Callable<String> readCode(String question) {
        return () -> Reader.ask(question);
    }




    public CompletableFuture<LoginResponse> sendLoginRequest() {
        return new QeSyncRequest().execute(this)
                .thenCompose(response -> {
                    String encryptedPassword = IGUtils.encryptPassword(
                            this.password,
                            this.encryptionId,
                            this.encryptionKey);

                    return new AccountsLoginRequest(username, encryptedPassword)
                            .execute(this);
                })
                .thenApply((response) -> {
                    this.setLoggedInState(response);
                    return response;
                });
    }

    public CompletableFuture<LoginResponse> sendLoginRequest(String code, String identifier) {
        return new QeSyncRequest().execute(this)
                .thenCompose(response -> {
                    String encryptedPassword = IGUtils.encryptPassword(
                            this.password,
                            this.encryptionId,
                            this.encryptionKey);

                    return new AccountsTwoFactorLoginRequest(username, encryptedPassword, code, identifier)
                            .execute(this);
                })
                .thenApply((response) -> {
                    if (response == null) {
                        throw new RuntimeException("Response is null");
                    }
                    this.setLoggedInState(response);
                    return response;
                });
    }

    public <T extends IGResponse> CompletableFuture<T> sendRequest(@NonNull IGRequest<T> request) {
        return sendRequestWithoutRetry(
                request,
                0
        );
    }

    public <T extends IGResponse> CompletableFuture<T> sendRequestWithoutRetry(@NonNull IGRequest<T> request, Integer retry) {
//        if(retry >= 3) {
//            CompletableFuture<T> failedFuture = new CompletableFuture<>();
//            failedFuture.completeExceptionally(
//                    new RuntimeException("Max retry attempts!")
//            );
//            return failedFuture;
//        }

        CompletableFuture<Pair<Response, String>> future = new CompletableFuture<>();

//        log.info("Sending request : {}", request.formUrl(this).toString());

        this.httpClient
                .newCall(request.formRequest(this))
                .enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response res) throws IOException {
//                        log.info("Response for {} : {}", call.request().url().toString(), res.code());
                        try (ResponseBody body = res.body()) {
                            future.complete(new Pair<>(res, body.string()));
                        } catch (Exception exception) {
                            future.complete(new Pair<>(res, null));
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException exception) {
                        future.completeExceptionally(exception);
                    }
                });

        return future
                .thenApply(res -> {
                    Response response = res.getFirst();
                    HttpUrl url = response.request().url();
                    String body = res.getSecond();

                    setFromResponseHeaders(response);
                    log.info("[{}]: {}, Body: {}",
                            username,
                            url,
                            IGUtils.truncate(body));

                    try {
                        FileUtils.writeStringToFile(
                                new File("response/" + url.toString() + ".json"),
                                body,
                                Charset.defaultCharset());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return request.parseResponse(res);
                })
                .exceptionally(
                        (throwable) -> {
                            Throwable cause = throwable.getCause();
                            if ("Please wait a few minutes before you try again.".equals(cause.getMessage())) {
//                                InstagramUtils.sleep(60000);
//                                return sendRequestWithoutRetry(request, retry + 1).join();
                                throw new CompletionException("Please wait a few minutes before you try again.", cause);
                            }
                            if ("Sorry, there was a problem with your request.".equals(cause.getMessage())) {
                                throw new CompletionException("Sorry, there was a problem with your request.", cause);
                            }
//                            if ("User not found".equals(cause.getMessage())) {
////                                throw new SkipException(cause.getMessage(), cause);
//                                return null;
//                            }

                            log.error(throwable.getMessage());
                            throw new CompletionException(cause.getMessage(), cause);
                        });
    }



    private void setLoggedInState(LoginResponse response) {
        if (Objects.equals(response.getStatus(), OK)) {
            this.loggedIn = true;
            this.selfProfile = response.getLogged_in_user();
            log.info("Logged into {} ({})", selfProfile.username(), selfProfile.id());
        } else {
            boolean debug = true;
        }
    }

    public String getCsrfToken() {
        return IGUtils.getCookieValue(this.getHttpClient().cookieJar(), "csrftoken")
                .orElse("missing");
    }

    public void setFromResponseHeaders(Response res) {
        Optional.ofNullable(res.header("ig-set-password-encryption-key-id"))
                .ifPresent(s -> this.encryptionId = s);
        Optional.ofNullable(res.header("ig-set-password-encryption-pub-key"))
                .ifPresent(s -> this.encryptionKey = s);
        String authorisation = res.header("ig-set-authorization");
        Optional.ofNullable(authorisation)
                .ifPresent(s -> this.authorization = s);
        // Bearer IGT:2:eyJkc191c2VyX2lkIjoiMTQxNzgzMjc0NCIsInNlc3Npb25pZCI6IjE0MTc4MzI3NDQlM0F0U0JFb01McHEwdnR3NSUzQTUiLCJzaG91bGRfdXNlX2hlYWRlcl9vdmVyX2Nvb2tpZXMiOmZhbHNlfQ==
    }

    public IGPayload setIGPayloadDefaults(IGPayload load) {
        load.set_csrftoken(this.getCsrfToken());
        load.setDevice_id(this.deviceId);
        if (selfProfile != null) {
            load.set_uid(selfProfile.id());
            load.set_uuid(this.guid);
        } else {
            load.setId(this.guid);
        }
        load.setGuid(this.guid);
        load.setPhone_id(this.phoneId);

        return load;
    }

//    public static IGClient from(InputStream from) throws ClassNotFoundException, IOException {
//        return from(from, IGUtils.defaultHttpClientBuilder().build());
//    }
//
//    public static IGClient from(InputStream from, @NonNull OkHttpClient httpClient)
//            throws IOException, ClassNotFoundException {
//        try (ObjectInputStream in = new ObjectInputStream(from)) {
//            IGClient client = (IGClient) in.readObject();
//            client.httpClient = httpClient;
//
//            return client;
//        }
//    }

    public IGClientActions actions() {
        return this.actions;
    }


    public void markUnavailable() {
        this.available = false;
        this.isNotAvailableFrom = LocalDateTime.now();
    }

    public void markAvailable() {
        this.available = true;
        this.isNotAvailableFrom = null;
    }

    public boolean isAvailable() {
        return this.available
                && isNotAvailableFrom == null;
    }

    public boolean isNotAvailable() {
        return !this.available
                && isNotAvailableFrom != null && isNotAvailableFrom.until(LocalDateTime.now(), ChronoUnit.MINUTES) > 1;
    }
}
