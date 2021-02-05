package com.stmgalex.reservation.exception;

public class NoAvaialableSeatsException extends RuntimeException {

  public NoAvaialableSeatsException(String message) {
    super(message);
  }
}
