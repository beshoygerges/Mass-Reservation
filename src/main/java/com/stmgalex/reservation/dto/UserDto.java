package com.stmgalex.reservation.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
public class UserDto implements Serializable {

    @JsonProperty(access = READ_ONLY)
    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nationalId;

    @NotEmpty
    private String mobileNumber;

    @JsonProperty(access = READ_ONLY)
    private Integer age;

    @JsonProperty(access = READ_ONLY)
    private LocalDate birthdate;
}
