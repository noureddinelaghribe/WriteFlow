package com.noureddine.WriteFlow.Utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataCoverter {

    public String longToDataWithNameMonthe(long timestamp){
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("MMMM dd, yyyy", Locale.ENGLISH)
                .withZone(ZoneId.of("UTC")); // Explicitly set UTC time zone
        return formatter.format(Instant.ofEpochMilli(timestamp));
    }

    public String longToData(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.of("UTC")); // Explicit UTC time zone [[7]]
        return formatter.format(instant);
    }

}
