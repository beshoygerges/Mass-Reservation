package com.stmgalex.reservation.util;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private DateUtil() {

    }

    public static LocalDate getBirthDate(String nationalId) {
        String yearPrefix = null;

        if (nationalId.startsWith("2")) {
            yearPrefix = "19";
        } else {
            yearPrefix = "20";
        }

        String birthdateStr = yearPrefix + nationalId.substring(1, 7);

        return LocalDate.parse(birthdateStr, formatter);

    }

    public static int calculateAge(LocalDate birthdate) {
        return (int) YEARS.between(birthdate, LocalDate.now());
    }
}
