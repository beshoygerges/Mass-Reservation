package com.stmgalex.reservation.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelReservationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "من فضلك ادخل رقم قومي صالحا")
    private String nationalId;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "من فضلك ادخل يوم القداس")
    private LocalDate massDate;

    @NotNull(message = "من فضلك ادخل وقت القداس")
    private LocalTime massTime;
}
