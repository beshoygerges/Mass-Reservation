package com.stmgalex.reservation.util;


import org.apache.commons.lang3.Range;

public final class RangeUtil {

    private final static Range<Integer> churchRange = Range.between(1, 300);
    private final static Range<Integer> runwayRange = Range.between(301, 375);
    private final static Range<Integer> theatreRange = Range.between(376, 525);

    private RangeUtil() {

    }

    public static String getRangePlace(int seatNumber) {
        if (churchRange.contains(seatNumber)) {
            return "الكنيسة";
        }

        if (runwayRange.contains(seatNumber)) {
            return "المدرج";
        }

        if (theatreRange.contains(seatNumber)) {
            return "المسرح";
        }

        return "الكنيسة";
    }

}
