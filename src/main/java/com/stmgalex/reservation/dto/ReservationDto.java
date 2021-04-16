package com.stmgalex.reservation.dto;

import com.stmgalex.reservation.entity.Evening;
import java.io.Serializable;
import lombok.Data;

@Data
public class ReservationDto implements Serializable {

    private Integer id;

    private MassDto mass;

    private Evening evening;

    private UserDto user;

    private boolean active;

    private int seatNumber;

    private String place;
}
