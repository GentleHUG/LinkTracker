package edu.java.bot.configuration;

import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import java.io.IOException;
import java.util.List;

public class CustomRetryPolicy extends SimpleRetryPolicy {

    private final List<Integer> retryableStatuses;

    public CustomRetryPolicy(List<Integer> retryableStatuses) {
        this.retryableStatuses = retryableStatuses;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        Throwable t = context.getLastThrowable();
        return (t == null || retryForException(t) && context.getRetryCount() < getMaxAttempts());
    }

    private boolean retryForException(Throwable throwable) {
        if (throwable instanceof HttpClientErrorException clientErrorException) {
            HttpStatusCode statusCode = clientErrorException.getStatusCode();
            return retryableStatuses.contains(statusCode.value());
        } else if (throwable instanceof HttpServerErrorException serverErrorException) {
            HttpStatusCode statusCode = serverErrorException.getStatusCode();
            return retryableStatuses.contains(statusCode.value());
        }

        return false;
    }
}
