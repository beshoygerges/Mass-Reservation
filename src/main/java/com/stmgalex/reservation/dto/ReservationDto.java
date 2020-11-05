package com.stmgalex.reservation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReservationDto implements Serializable {

    private Integer id;

    private MassDto mass;

    private UserDto user;

    private int seatNumber;

    private boolean active;
}
