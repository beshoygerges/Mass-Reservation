package com.stmgalex.reservation.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class EveningDto implements Serializable {

  @Min(1)
  private int id;

  @NotNull(message = "من فضلك ادخل يوم القداس")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  @NotNull(message = "من فضلك ادخل وقت القداس")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime time;

  @NotNull
  private Integer totalSeats;

  private int availableSeats;

  @NotNull
  private Integer reservedSeats;

  private boolean enabled;

  private boolean expired;

  private String status;

  private double attendancePercentage;

  private String day;

  private int age;

}
