package com.stmgalex.reservation.util;

import org.modelmapper.ModelMapper;

public final class MapperUtil {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private MapperUtil() {

    }

    public static <T> T map(Object source, Class<T> destination) {
        return MODEL_MAPPER.map(source, destination);
    }
}
