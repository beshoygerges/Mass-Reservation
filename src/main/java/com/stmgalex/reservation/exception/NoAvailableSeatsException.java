package com.stmgalex.reservation.exception;

public class NoAvailableSeatsException extends RuntimeException {

    public NoAvailableSeatsException(String message) {
        super(message);
    }
}
