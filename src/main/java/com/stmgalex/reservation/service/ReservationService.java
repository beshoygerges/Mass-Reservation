package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.*;
import com.stmgalex.reservation.entity.Mass;

import javax.validation.Valid;

public interface ReservationService {
    ReservationResponse reserve(ReservationRequest request);

    ReservationResponse cancelReservation(@Valid CancelReservationRequest request);

    ReservationResponse searchReservation(@Valid SearchReservationRequest request);

    Mass getAvailableSeats(AvailableSeatsRequest request);
}
