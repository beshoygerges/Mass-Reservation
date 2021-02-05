package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.CancelReservationRequest;
import com.stmgalex.reservation.dto.EveningReservationDisableRequest;
import com.stmgalex.reservation.dto.EveningReservationRequest;
import com.stmgalex.reservation.dto.MassReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.dto.SearchReservationRequest;
import com.stmgalex.reservation.entity.Mass;
import javax.validation.Valid;

public interface ReservationService {

  ReservationResponse reserve(MassReservationRequest request);

  ReservationResponse cancelReservation(@Valid CancelReservationRequest request);

  ReservationResponse searchReservation(@Valid SearchReservationRequest request);

  Mass getAvailableSeats(AvailableSeatsRequest request);

  ReservationResponse reserve(EveningReservationRequest request);

  ReservationResponse cancelReservation(EveningReservationDisableRequest request);
}
