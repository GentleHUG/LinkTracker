package edu.java.bot.util;

import lombok.experimental.UtilityClass;
import java.net.URL;

@UtilityClass
public class UrlChecker {
    public boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
