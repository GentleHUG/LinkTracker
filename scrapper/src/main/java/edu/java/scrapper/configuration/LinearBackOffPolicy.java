package edu.java.scrapper.configuration;

import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.ThreadWaitSleeper;

public class LinearBackOffPolicy implements BackOffPolicy {

    private long initialDelay = 1000L;
    private long maxDelay = Long.MAX_VALUE;
    private final Sleeper sleeper = new ThreadWaitSleeper();

    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackOffContext();
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        LinearBackOffContext context = (LinearBackOffContext) backOffContext;
        try {
            sleeper.sleep(Math.min(initialDelay * context.attemptsCount, maxDelay));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
        context.attemptsCount++;
    }

    private static class LinearBackOffContext implements BackOffContext {
        private int attemptsCount = 1;
    }
}
