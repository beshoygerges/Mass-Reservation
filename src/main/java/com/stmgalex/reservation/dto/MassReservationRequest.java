package com.stmgalex.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
public class MassReservationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "من فضلك ادخل رقم قومي صالحا")
    private String nationalId;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "من فضلك ادخل يوم القداس")
    private LocalDate massDate;

    @NotNull(message = "من فضلك ادخل وقت القداس")
    private LocalTime massTime;


}
