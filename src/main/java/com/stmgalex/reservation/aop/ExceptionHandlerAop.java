package com.stmgalex.reservation.aop;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.stmgalex.reservation.dto.Response;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAop extends ResponseEntityExceptionHandler {

  // error handle for @Valid
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("status", status.value());

    //Get all errors
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(x -> x.getDefaultMessage())
        .collect(Collectors.toList());

    body.put("message", errors);

    return new ResponseEntity<>(body, headers, status);

  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(RuntimeException.class)
  public Response handleException(RuntimeException e) {
    log.error(e.getMessage());
    return new Response(400, e.getMessage());
  }


}
