package edu.java.scrapper.exception;

public class ExistsLinkException extends Exception{
    public ExistsLinkException() {
        super("Link already exists");
    }
}
