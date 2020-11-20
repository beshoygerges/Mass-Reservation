package com.stmgalex.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
public class MassReservationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "من فضلك ادخل الاسم")
    private String name;

    @NotEmpty(message = "من فضلك ادخل رقم قومي صالحا")
    private String nationalId;

    @Pattern(regexp = "^(010|011|012|015)[0-9]{8}$", message = "من فضلك ادخل رقم موبايل صالح")
    @NotEmpty(message = "من فضلك ادخل رقم موبايل صالح")
    private String mobileNumber;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "من فضلك ادخل يوم القداس")
    private LocalDate massDate;

    @NotNull(message = "من فضلك ادخل وقت القداس")
    private LocalTime massTime;


}
