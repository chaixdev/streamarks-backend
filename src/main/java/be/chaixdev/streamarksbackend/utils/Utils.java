package be.chaixdev.streamarksbackend.utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String getNow() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }
}
