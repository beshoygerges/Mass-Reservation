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

    private int availableSeats;

    private int reservedSeats;

    private boolean enabled;

    private String status;

    private double attendancePercentage;

}
