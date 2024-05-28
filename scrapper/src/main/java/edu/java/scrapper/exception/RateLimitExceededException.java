package edu.java.scrapper.exception;

public class RateLimitExceededException extends Exception {
    public RateLimitExceededException() {
        super("Too many requests in this time");
    }
}
