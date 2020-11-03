package com.stmgalex.reservation.controller;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.ReservationRequest;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.service.ReservationService;
import com.stmgalex.reservation.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Validated
@AllArgsConstructor
@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity addReservation(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.reserve(request));
    }

    @GetMapping
    public ResponseEntity getReservation(@RequestParam @NotEmpty String nationalId) {
        return ResponseEntity.ok(reservationService.searchReservation(nationalId));
    }

    @DeleteMapping
    public ResponseEntity deleteReservation(@RequestParam @NotEmpty String nationalId) {
        return ResponseEntity.ok(reservationService.cancelReservation(nationalId));
    }

    @PostMapping("/seats")
    public ResponseEntity getMassAvailableSeats(@Valid @RequestBody AvailableSeatsRequest request) {
        Mass mass = reservationService.getAvailableSeats(request);
        return ResponseEntity.ok(MapperUtil.map(mass, MassDto.class));
    }
}
