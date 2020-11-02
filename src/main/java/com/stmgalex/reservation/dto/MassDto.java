package com.stmgalex.reservation.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MassDto implements Serializable {

    private Integer id;

    private LocalDate date;

    private LocalTime time;

    private int totalSeats;

    private int reservedSeats;

    private boolean enabled;

    public String status() {
        return enabled ? "نشط" : "غير نشط";
    }

    public double getAttendancePercentage() {
        return reservedSeats * 1.0 / totalSeats * 100d;
    }
}
