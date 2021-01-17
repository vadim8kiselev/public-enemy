package com.kiselev.enemy.network.instagram.api.internal2.actions.async;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncAction {
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(0);

    public static Executor delayedExecutor(long delay, TimeUnit unit) {
        return delayedExecutor(delay, unit, ForkJoinPool.commonPool());
    }

    public static Executor delayedExecutor(long delay, TimeUnit unit, Executor executor) {
        return r -> SCHEDULER.schedule(() -> executor.execute(r), delay, unit);
    }

    public static List<CompletableFuture<?>> executeRequestsAsync(IGClient client,
            IGRequest<?>... reqs) {
        return Stream.of(reqs).map(client::sendRequest).collect(Collectors.toList());
    }

    public static <T> CompletableFuture<T> retry(Supplier<CompletableFuture<T>> action,
            Throwable error,
            int tries, long delay, TimeUnit unit) {
        if (tries == 0) {
            CompletableFuture<T> failed = new CompletableFuture<T>();
            failed.completeExceptionally(error);

            return failed;
        }
        return action.get()
                .thenApply(CompletableFuture::completedFuture)
                .exceptionally(tr -> {
                    error.addSuppressed(tr);

                    return retry(action, error, tries - 1, delay, unit);
                })
                .thenComposeAsync(Function.identity(), delayedExecutor(delay, unit));
    }

}
