package com.stmgalex.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Statistics implements Serializable {
    private long masses;
    private long reservations;
    private long users;
    private long canceledReservations;
    private long completedMasses;
    private long approvedReservations;
    private double attendancePercentage;
}