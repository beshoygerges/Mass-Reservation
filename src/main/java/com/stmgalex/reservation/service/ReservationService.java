package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.ReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;

public interface ReservationService {
    ReservationResponse reserve(ReservationRequest request);

    ReservationResponse cancelReservation(String nationalId);

    ReservationResponse searchReservation(String nationalId);
}
