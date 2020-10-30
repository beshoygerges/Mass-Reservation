package com.stmgalex.reservation.exception;

public class MassHaveNoSeatsException extends RuntimeException {

    public MassHaveNoSeatsException(String message) {
        super(message);
    }
}
