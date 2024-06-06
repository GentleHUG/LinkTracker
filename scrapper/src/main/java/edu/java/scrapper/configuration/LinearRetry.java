package edu.java.scrapper.configuration;

import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Predicate;

public final class LinearRetry extends Retry {

    private final int maxAttempts;
    private final long initialInterval;
    private final long maxInterval = Long.MAX_VALUE;

    private final Predicate<Throwable> errorFilter;

    private LinearRetry(int maxAttempts, long initialInterval, Predicate<? super Throwable> errorFilter) {
        this.maxAttempts = maxAttempts;
        this.initialInterval = initialInterval;
        this.errorFilter = errorFilter::test;
    }

    public static LinearRetry linearBackoff(int maxAttempts, Duration minDelay) {
        return new LinearRetry(maxAttempts, minDelay.toMillis(), e -> true);
    }

    public LinearRetry filter(Predicate<? super Throwable> errorFilter) {
        return new LinearRetry(
            maxAttempts,
            initialInterval,
            Objects.requireNonNull(errorFilter)
        );
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::getRetry);
    }

    private Mono<Long> getRetry(RetrySignal retrySignal) {
        long retries = retrySignal.totalRetries() + 1;
        if (retries < maxAttempts && errorFilter.test(retrySignal.failure())) {
            Duration delay = Duration.ofMillis(Math.min(initialInterval * retries, maxInterval));
            return Mono.delay(delay).thenReturn(retrySignal.totalRetries());
        } else {
            throw Exceptions.propagate(retrySignal.failure());
        }
    }
}
