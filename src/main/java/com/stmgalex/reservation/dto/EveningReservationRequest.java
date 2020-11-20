package com.stmgalex.reservation.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class EveningReservationRequest implements Serializable {

    @NotEmpty(message = "من فضلك ادخل الاسم")
    private String name;

    @NotEmpty(message = "من فضلك ادخل رقم قومي صالحا")
    private String nationalId;

    @Pattern(regexp = "^(010|011|012|015)[0-9]{8}$", message = "من فضلك ادخل رقم موبايل صالح")
    @NotEmpty(message = "من فضلك ادخل رقم موبايل صالح")
    private String mobileNumber;

    @NotNull(message = "من فضلك اختر السهرة")
    private Integer eveningId;
}
