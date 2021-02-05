package com.stmgalex.reservation.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EveningReservationDisableRequest implements Serializable {

  @NotEmpty(message = "من فضلك ادخل رقم قومي صالحا")
  private String nationalId;

  @NotNull(message = "من فضلك ادخل السهرة")
  private Integer eveningId;
}
