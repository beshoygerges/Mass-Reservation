package com.stmgalex.reservation.dto;

import com.stmgalex.reservation.entity.Evening;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReservationDto implements Serializable {

    private Integer id;

    private MassDto mass;

    private Evening evening;

    private UserDto user;

    private boolean active;
}
