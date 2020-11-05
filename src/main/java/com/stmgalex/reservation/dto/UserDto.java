package com.stmgalex.reservation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private Integer id;

    private String name;

    private String nationalId;

    private String mobileNumber;
}
