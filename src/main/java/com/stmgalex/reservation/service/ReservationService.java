package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.ReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.entity.Mass;

public interface ReservationService {
    ReservationResponse reserve(ReservationRequest request);

    ReservationResponse cancelReservation(String nationalId);

    ReservationResponse searchReservation(String nationalId);

    Mass getAvailableSeats(AvailableSeatsRequest request);
}
