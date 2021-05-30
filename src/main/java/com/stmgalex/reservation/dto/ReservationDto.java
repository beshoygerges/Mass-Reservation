package com.stmgalex.reservation.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ReservationDto implements Serializable {

    private Integer id;

    private MassDto mass;

    private UserDto user;

    private boolean active;

    private int seatNumber;

    private String place;
}
