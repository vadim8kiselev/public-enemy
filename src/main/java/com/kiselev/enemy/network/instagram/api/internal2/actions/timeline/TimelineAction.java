package com.kiselev.enemy.network.instagram.api.internal2.actions.timeline;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.actions.feed.FeedIterable;
import com.kiselev.enemy.network.instagram.api.internal2.actions.media.MediaAction;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.UploadParameters;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedTimelineRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureSidecarRequest.MediaConfigureSidecarPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureSidecarRequest.SidecarChildrenMetadata;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaConfigureTimelineRequest.MediaConfigurePayload;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedTimelineResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaResponse.MediaConfigureSidecarResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaResponse.MediaConfigureTimelineResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.RuploadPhotoResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TimelineAction {
    @NonNull
    private IGClient client;

    public FeedIterable<FeedTimelineRequest, FeedTimelineResponse> feed() {
        return new FeedIterable<>(client, FeedTimelineRequest::new);
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadPhoto(byte[] data,
            MediaConfigurePayload payload) {
        return client.actions().upload()
                .photo(data, String.valueOf(System.currentTimeMillis()))
                .thenCompose(res -> MediaAction.configureMediaToTimeline(client, res.getUpload_id(), payload));
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadPhoto(byte[] data,
            String caption) {
        return uploadPhoto(data, new MediaConfigurePayload().caption(caption));
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadPhoto(File file,
            String caption) {
        return uploadPhoto(file,
                new MediaConfigurePayload().caption(caption));
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadPhoto(File file,
            MediaConfigurePayload payload) {
        try {
            return uploadPhoto(Files.readAllBytes(file.toPath()), payload);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadVideo(byte[] videoData,
            byte[] coverData, MediaConfigurePayload payload) {
        String upload_id = String.valueOf(System.currentTimeMillis());
        return client.actions().upload()
                .videoWithCover(videoData, coverData,
                        UploadParameters.forTimelineVideo(upload_id, false))
                .thenCompose(response -> {
                    return client.actions().upload().finish(upload_id);
                })
                .thenCompose(response -> MediaAction.configureMediaToTimeline(client, upload_id, payload));
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadVideo(File video, File cover,
            String caption) {
        return uploadVideo(video, cover, new MediaConfigurePayload().caption(caption));
    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadVideo(File video, File cover,
            MediaConfigurePayload payload) {
        try {
            return uploadVideo(Files.readAllBytes(video.toPath()),
                    Files.readAllBytes(cover.toPath()), payload);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }

    }

    public CompletableFuture<MediaConfigureTimelineResponse> uploadVideo(byte[] videoData,
            byte[] coverData, String caption) {
        return uploadVideo(videoData, coverData, new MediaConfigurePayload().caption(caption));
    }

    public CompletableFuture<MediaConfigureSidecarResponse> uploadAlbum(List<SidecarInfo> infos,
            MediaConfigureSidecarPayload payload) {
        List<CompletableFuture<?>> uploadFutures =
                infos.stream().map(s -> s.upload(client)).collect(Collectors.toList());
        payload.children_metadata()
                .addAll(infos.stream().map(SidecarInfo::metadata).collect(Collectors.toList()));
        return CompletableFuture
                .allOf(uploadFutures.toArray(new CompletableFuture[uploadFutures.size()]))
                .thenCompose(res -> MediaAction.configureAlbumToTimeline(client, payload));
    }

    public CompletableFuture<MediaConfigureSidecarResponse> uploadAlbum(List<SidecarInfo> infos,
            String caption) {
        return uploadAlbum(infos, new MediaConfigureSidecarPayload().caption(caption));
    }

    public static interface SidecarInfo {
        CompletableFuture<? extends IGResponse> upload(IGClient client);

        SidecarChildrenMetadata metadata();
    }

    @Data
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class SidecarPhoto implements SidecarInfo {
        @NonNull
        private byte[] data;
        private SidecarChildrenMetadata metadata =
                new SidecarChildrenMetadata(String.valueOf(System.currentTimeMillis()));

        @Override
        public CompletableFuture<RuploadPhotoResponse> upload(IGClient client) {
            return client.actions().upload().photo(data, metadata.upload_id(), true);
        }

        public static SidecarPhoto from(File file) {
            try {
                return new SidecarPhoto(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public static SidecarPhoto from(File file, SidecarChildrenMetadata metadata) {
            try {
                return new SidecarPhoto(Files.readAllBytes(file.toPath())).metadata(metadata);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Data
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class SidecarVideo implements SidecarInfo {
        @NonNull
        private byte[] data;
        @NonNull
        private byte[] cover_data;
        private SidecarChildrenMetadata metadata =
                new SidecarChildrenMetadata(String.valueOf(System.currentTimeMillis()));

        @Override
        public CompletableFuture<? extends IGResponse> upload(IGClient client) {
            return client.actions().upload().videoWithCover(data, cover_data,
                    UploadParameters.forTimelineVideo(metadata.upload_id(), true));
        }

        public static SidecarVideo from(File video, File cover) {
            try {
                return new SidecarVideo(Files.readAllBytes(video.toPath()),
                        Files.readAllBytes(cover.toPath()));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public static SidecarVideo from(File video, File cover, SidecarChildrenMetadata metadata) {
            try {
                return new SidecarVideo(Files.readAllBytes(video.toPath()),
                        Files.readAllBytes(cover.toPath())).metadata(metadata);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
