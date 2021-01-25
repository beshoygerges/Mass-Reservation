package com.stmgalex.reservation.controller.user;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.CancelReservationRequest;
import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.MassReservationRequest;
import com.stmgalex.reservation.dto.SearchReservationRequest;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.service.ReservationService;
import com.stmgalex.reservation.util.MapperUtil;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@AllArgsConstructor
@Controller
@RequestMapping("/reservations/masses")
public class MassReservationController {

  private final ReservationService reservationService;

  @PostMapping
  public ResponseEntity addReservation(@Valid @RequestBody MassReservationRequest request) {
    return ResponseEntity.ok(reservationService.reserve(request));
  }

  @PostMapping("/search")
  public ResponseEntity getReservation(@Valid @RequestBody SearchReservationRequest request) {
    return ResponseEntity.ok(reservationService.searchReservation(request));
  }

  @PostMapping("/delete")
  public ResponseEntity cancelReservation(@Valid @RequestBody CancelReservationRequest request) {
    return ResponseEntity.ok(reservationService.cancelReservation(request));
  }

  @PostMapping("/seats")
  public ResponseEntity getAvailableSeats(@Valid @RequestBody AvailableSeatsRequest request) {
    Mass mass = reservationService.getAvailableSeats(request);
    return ResponseEntity.ok(MapperUtil.map(mass, MassDto.class));
  }
}
