package edu.java.bot.utilTests;

import edu.java.bot.util.UrlChecker;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class UrlCheckerTest {

    @Test
    @DisplayName("Проверка корректной ссылки")
    public void testCorrectURL() {
        String link = "https://github.com/";
        boolean result = UrlChecker.isValid(link);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Проверка некорректной ссылки")
    public void testIncorrectURL() {
        String link = "http//s:ervedf/vss";
        boolean result = UrlChecker.isValid(link);
        Assertions.assertFalse(result);
    }
}
