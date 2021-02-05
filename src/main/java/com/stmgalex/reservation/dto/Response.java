package com.stmgalex.reservation.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class Response implements Serializable {

  private int code;
  private String message;
  private Object result;

  public Response(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
