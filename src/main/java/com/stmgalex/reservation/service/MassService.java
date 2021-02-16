package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.CancelReservationRequest;
import com.stmgalex.reservation.dto.MassReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.dto.SearchReservationRequest;
import com.stmgalex.reservation.entity.Mass;
import java.time.LocalDate;
import java.util.List;

public interface MassService {

    ReservationResponse reserve(MassReservationRequest request);

    ReservationResponse cancelReservation(CancelReservationRequest request);

    ReservationResponse searchReservation(SearchReservationRequest request);

    Mass getAvailableSeats(AvailableSeatsRequest request);

    List<Mass> getMassesWithDate(LocalDate localDate);
}
