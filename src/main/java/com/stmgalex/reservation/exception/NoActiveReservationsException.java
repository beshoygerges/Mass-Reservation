package com.stmgalex.reservation.exception;

public class NoActiveReservationsException extends RuntimeException {

  public NoActiveReservationsException(String message) {
    super(message);
  }
}
