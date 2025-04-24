package com.noureddine.WriteFlow.Utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataCoverter {

    public static String longToDataWithNameMonthe(long timestamp){

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("MMMM dd, yyyy", Locale.ENGLISH)
                .withZone(ZoneId.of("UTC")); // Explicitly set UTC time zone

        if (String.valueOf(timestamp).length()==13){
            //milliseconds
            return formatter.format(Instant.ofEpochMilli(timestamp));
        }else {
            //seconds
            return formatter.format(Instant.ofEpochMilli(timestamp * 1000L));
        }

    }

    public static String longToData(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.of("UTC")); // Explicit UTC time zone [[7]]
        return formatter.format(instant);
    }

    public static long dataToLong(int year, int month, int day, int hour, int minute, int seconds){
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, seconds);
        // Convert to Unix time assuming UTC. Change ZoneOffset if needed.
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }

}
