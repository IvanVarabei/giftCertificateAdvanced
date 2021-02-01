package com.epam.esm.config;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeZoneConfig {
    public static final ZoneId CLIENT_ZONE = ZoneId.of("Europe/Moscow");
    public static final ZoneId DATABASE_ZONE = ZoneOffset.UTC;
}
