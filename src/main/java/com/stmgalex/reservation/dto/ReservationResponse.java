package com.stmgalex.reservation.dto;

import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.Reservation;
import com.stmgalex.reservation.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponse implements Serializable {

    private int reservationId;

    private String name;

    private LocalDate massDate;

    private LocalTime massTime;

    public ReservationResponse(Reservation reservation) {
        User user = reservation.getUser();
        Mass mass = reservation.getMass();
        setMassDate(mass.getDate());
        setMassTime(mass.getTime());
        setName(user.getName());
        setReservationId(reservation.getId());
    }
}
