package com.stmgalex.reservation.controller.user;

import com.stmgalex.reservation.dto.*;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.service.ReservationService;
import com.stmgalex.reservation.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Validated
@AllArgsConstructor
@Controller
@RequestMapping("/reservations/evenings")
public class EveningReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity addReservation(@Valid @RequestBody EveningReservationRequest request) {
        return ResponseEntity.ok(reservationService.reserve(request));
    }

    @PostMapping("/search")
    public ResponseEntity getReservation(@Valid @RequestBody SearchReservationRequest request) {
        return ResponseEntity.ok(reservationService.searchReservation(request));
    }

    @PostMapping("/disable")
    public ResponseEntity cancelReservation(@Valid @RequestBody EveningReservationDisableRequest request) {
        return ResponseEntity.ok(reservationService.cancelReservation(request));
    }

    @PostMapping("/seats")
    public ResponseEntity getMassAvailableSeats(@Valid @RequestBody AvailableSeatsRequest request) {
        Mass mass = reservationService.getAvailableSeats(request);
        return ResponseEntity.ok(MapperUtil.map(mass, MassDto.class));
    }
}
