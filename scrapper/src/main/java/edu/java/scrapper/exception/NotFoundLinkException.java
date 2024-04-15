package edu.java.scrapper.exception;

public class NotFoundLinkException extends Exception{
    public NotFoundLinkException() {
        super("Link was not found");
    }
}
