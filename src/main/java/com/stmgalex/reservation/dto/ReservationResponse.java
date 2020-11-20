package com.stmgalex.reservation.dto;

import com.stmgalex.reservation.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponse implements Serializable {

    private int reservationId;

    private String name;

    private int seatNumber;

    private LocalDate eveningDate;

    private LocalDate massDate;

    private LocalTime massTime;

    public ReservationResponse(MassReservation massReservation) {
        User user = massReservation.getUser();
        Mass mass = massReservation.getMass();
        setMassDate(mass.getDate());
        setMassTime(mass.getTime());
        setName(user.getName());
        setReservationId(massReservation.getId());
        setSeatNumber(massReservation.getSeatNumber());
    }

    public ReservationResponse(EveningReservation eveningReservation) {
        User user = eveningReservation.getUser();
        Evening evening = eveningReservation.getEvening();
        setEveningDate(evening.getDate());
        setName(user.getName());
        setReservationId(eveningReservation.getId());
    }
}
