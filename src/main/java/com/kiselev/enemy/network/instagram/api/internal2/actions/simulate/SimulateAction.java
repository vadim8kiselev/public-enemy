package com.kiselev.enemy.network.instagram.api.internal2.actions.simulate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.actions.async.AsyncAction;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsContactPointPrefillRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.accounts.AccountsGetPrefillCandidatesRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.direct.DirectGetPresenceRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.direct.DirectInboxRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.discover.DiscoverTopicalExploreRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedReelsTrayRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.feed.FeedTimelineRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.launcher.LauncherSyncRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.linkedaccounts.LinkedAccountsGetLinkageStatusRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.loom.LoomFetchConfigRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.media.MediaBlockedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.multipleaccounts.MultipleAccountsGetAccountFamilyRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.news.NewsInboxRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.qe.QeSyncRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.qp.QpGetCooldowns;
import com.kiselev.enemy.network.instagram.api.internal2.requests.status.StatusGetViewableStatusesRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersArlinkDownloadInfoRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimulateAction {
    @NonNull
    private IGClient client;

    private static final IGRequest<?>[] preLoginFlow = {
            new LauncherSyncRequest(true),
            new QeSyncRequest(true),
            new AccountsContactPointPrefillRequest(),
            new AccountsGetPrefillCandidatesRequest()
    };

    private static final IGRequest<?>[] postLoginFlow = {
            new LauncherSyncRequest(),
            new QpGetCooldowns(),
            new MultipleAccountsGetAccountFamilyRequest(),
            new LinkedAccountsGetLinkageStatusRequest(),
            new LoomFetchConfigRequest(),
            new MediaBlockedRequest(),
            new FeedTimelineRequest(),
            new FeedReelsTrayRequest(),
            new UsersArlinkDownloadInfoRequest(),
            new DiscoverTopicalExploreRequest().is_prefetch(true),
            new NewsInboxRequest(false),
            new DirectGetPresenceRequest(),
            new DirectInboxRequest().limit(0).visual_message_return_type("unseen")
                    .persistent_badging(true),
            new DirectInboxRequest().limit(20).fetch_reason("initial_snapshot")
                    .thread_message_limit(10).visual_message_return_type("unseen")
                    .persistent_badging(true),
            new StatusGetViewableStatusesRequest()
    };

    public List<CompletableFuture<?>> preLoginFlow() {
        return AsyncAction.executeRequestsAsync(client, preLoginFlow);
    }

    public List<CompletableFuture<?>> postLoginFlow() {
        return AsyncAction.executeRequestsAsync(client, postLoginFlow);
    }
}
