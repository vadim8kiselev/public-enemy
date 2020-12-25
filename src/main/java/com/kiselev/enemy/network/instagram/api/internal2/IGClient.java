package com.kiselev.enemy.network.instagram.api.internal2;

import com.kiselev.enemy.network.instagram.api.internal2.actions.IGClientActions;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.ExceptionallyHandler;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGChallengeRequiredException;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGLoginException;
import com.kiselev.enemy.network.instagram.api.internal2.exceptions.IGResponseException.IGFailedResponse;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsCurrentUserRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsLoginRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsTwoFactorLoginRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.qe.QeSyncRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.accounts.LoginResponse;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGChallengeUtils;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

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

    public IGClient(String username, String password) {
        this.username = username;
        this.password = password;
        this.guid = IGUtils.randomUuid();
        this.phoneId = IGUtils.randomUuid();
        this.deviceId = IGUtils.generateDeviceId(username, password);
        this.httpClient = IGUtils.defaultHttpClientBuilder().build();
        this.device = IGAndroidDevice.GOOD_DEVICES[0];
        this.sessionId = IGUtils.randomUuid();
        this.actions = new IGClientActions(this);
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
                () -> readFromKeyboard("Input two factors security code: "));
    }

    private LoginResponse authenticateWithChallenge(IGClient client, LoginResponse response) {
        return IGChallengeUtils.resolveChallenge(
                client,
                response,
                () -> readFromKeyboard("Input challenge security code: "));
    }

    private String readFromKeyboard(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
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
                    this.setLoggedInState(response);
                    return response;
                });
    }

    public <T extends IGResponse> CompletableFuture<T> sendRequest(@NonNull IGRequest<T> request) {
        CompletableFuture<Pair<Response, String>> responseFuture = new CompletableFuture<>();
        log.info("Sending request : {}", request.formUrl(this).toString());
        this.httpClient
                .newCall(request.formRequest(this))
                .enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response res) throws IOException {
                        log.info("Response for {} : {}", call.request().url().toString(), res.code());
                        try (ResponseBody body = res.body()) {
                            responseFuture.complete(new Pair<>(res, body.string()));
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException exception) {
                        responseFuture.completeExceptionally(exception);
                    }
                });

        return responseFuture
                .thenApply(res -> {
                    setFromResponseHeaders(res.getFirst());
                    log.info("Response for {} with body (truncated) : {}",
                            res.getFirst().request().url(),
                            IGUtils.truncate(res.getSecond()));

                    return request.parseResponse(res);
                })
                .exceptionally(
                        (throwable) -> {
//                Throwable cause = throwable.getCause();
//                if (cause instanceof IGChallengeRequiredException) {
//                    LoginResponse response = ((IGChallengeRequiredException) cause).getResponse();
//                    if (response.getChallenge() != null) {
//                        response = IGClient.this.authenticateWithChallenge(IGClient.this, response);
//                        IGClient.this.setLoggedInState(response);
//                    }
//                }

                            log.error(throwable.getMessage());
                            return null;
//                throw new CompletionException(throwable.getCause());
                        });

//                        (throwable) -> this.exceptionallyHandler.handle(throwable, request.getResponseType()));
    }


    private void setLoggedInState(LoginResponse response) {
        if (Objects.equals(response.getStatus(), OK)) {
            this.loggedIn = true;
            this.selfProfile = response.getLogged_in_user();
            log.info("Logged into {} ({})", selfProfile.getUsername(), selfProfile.getPk());
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
        Optional.ofNullable(res.header("ig-set-authorization"))
                .ifPresent(s -> this.authorization = s);
    }

    public IGPayload setIGPayloadDefaults(IGPayload load) {
        load.set_csrftoken(this.getCsrfToken());
        load.setDevice_id(this.deviceId);
        if (selfProfile != null) {
            load.set_uid(selfProfile.getPk().toString());
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

    @FunctionalInterface
    public interface LoginHandler {
        LoginResponse accept(IGClient client, LoginResponse response);
    }
}
