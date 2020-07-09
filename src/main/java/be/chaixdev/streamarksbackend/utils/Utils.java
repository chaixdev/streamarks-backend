package be.chaixdev.streamarksbackend.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String getNow() {
        DateTimeFormatter isoInstant = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return ZonedDateTime.now().format(isoInstant);
    }
}
