package com.stmgalex.reservation.util;

public final class NumberUtil {

    public static boolean isWithinRange(int start, int end, int number) {
        return start <= number && number <= end;
    }
}
