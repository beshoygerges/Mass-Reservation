package com.stmgalex.reservation.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class EveningReservationDisableRequest implements Serializable {

    @NotEmpty(message = "من فضلك ادخل رقم قومي صالحا")
    private String nationalId;

    @NotNull(message = "من فضلك ادخل السهرة")
    private Integer eveningId;
}
