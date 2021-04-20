package com.stmgalex.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import com.stmgalex.reservation.entity.User;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ReservationResponse implements Serializable {

    private int reservationId;

    private String name;

    private int seatNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate massDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
    private LocalTime massTime;

    private String place;

    public ReservationResponse(MassReservation massReservation) {
        User user = massReservation.getUser();
        Mass mass = massReservation.getMass();
        setMassDate(mass.getDate());
        setMassTime(mass.getTime());
        setName(user.getName());
        setReservationId(massReservation.getId());
        setSeatNumber(massReservation.getSeatNumber());
        setPlace(massReservation.getPlace());
    }

}
