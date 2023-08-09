package ca.sxxxi.twitter_clone_backend.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeProvider {
    public static Long epochSecondsNow() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
