package com.kiselev.enemy.network.instagram.api.internal2.actions.media;

import java.util.concurrent.CompletableFuture;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.actions.feed.FeedIterable;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaActionRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaCommentRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureSidecarRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureSidecarRequest.MediaConfigureSidecarPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureTimelineRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureTimelineRequest.MediaConfigurePayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureToIgtvRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaEditRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaGetCommentsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaInfoRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaGetCommentsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaInfoResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaResponse.MediaConfigureSidecarResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaResponse.MediaConfigureTimelineResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaResponse.MediaConfigureToIgtvResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaAction {
    @NonNull
    private IGClient client;
    @NonNull
    private String media_id;
    
    public CompletableFuture<IGResponse> comment(String comment) {
        return new MediaCommentRequest(media_id, comment).execute(client);
    }
    
    public CompletableFuture<MediaResponse> editCaption(String caption) {
        return new MediaEditRequest(media_id, caption).execute(client);
    }
    
    public CompletableFuture<MediaInfoResponse> info() {
        return new MediaInfoRequest(media_id).execute(client);
    }
    
    public FeedIterable<MediaGetCommentsRequest, MediaGetCommentsResponse> comments() {
        return new FeedIterable<>(client, () -> new MediaGetCommentsRequest(media_id));
    }
    
    public CompletableFuture<IGResponse> action(MediaActionRequest.MediaAction action) {
        return new MediaActionRequest(media_id, action).execute(client);
    }
    
    public static MediaAction of(IGClient client, String media_id) {
        return new MediaAction(client, media_id);
    }
    
    public static CompletableFuture<MediaConfigureTimelineResponse> configureMediaToTimeline(IGClient client, String upload_id, MediaConfigurePayload payload) {
        return new MediaConfigureTimelineRequest(payload.upload_id(upload_id)).execute(client);
    }
    
    public static CompletableFuture<MediaConfigureSidecarResponse> configureAlbumToTimeline(IGClient client, MediaConfigureSidecarPayload payload) {
        return new MediaConfigureSidecarRequest(payload).execute(client);
    }
    
    public static CompletableFuture<MediaConfigureToIgtvResponse> configureToIgtv(IGClient client, String upload_id, String title, String caption, boolean postToFeed) {
        return new MediaConfigureToIgtvRequest(upload_id, title, caption, postToFeed).execute(client);
    }
}
