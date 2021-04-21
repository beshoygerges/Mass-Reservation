package com.stmgalex.reservation.controller.user;

import com.google.zxing.WriterException;
import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.CancelReservationRequest;
import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.MassReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.dto.SearchReservationRequest;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.service.MassService;
import com.stmgalex.reservation.util.MapperUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@AllArgsConstructor
@Controller
@RequestMapping("/reservations/masses")
public class MassReservationController {

    private final MassService massService;
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";

    @PostMapping
    public ResponseEntity addReservation(@Valid @RequestBody MassReservationRequest request)
        throws IOException, WriterException {
        return ResponseEntity.ok(massService.reserve(request));
    }

    @PostMapping("/search")
    public ResponseEntity getReservation(@Valid @RequestBody SearchReservationRequest request) {
        return ResponseEntity.ok(massService.searchReservation(request));
    }

    @PostMapping("/search/qr")
    public ResponseEntity<ReservationResponse> getReservationQR(
        @Valid @RequestBody SearchReservationRequest request)
        throws IOException, WriterException {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(massService.searchReservationQR(request));
    }

    @PostMapping("/delete")
    public ResponseEntity cancelReservation(@Valid @RequestBody CancelReservationRequest request) {
        return ResponseEntity.ok(massService.cancelReservation(request));
    }

    @PostMapping("/seats")
    public ResponseEntity getAvailableSeats(@Valid @RequestBody AvailableSeatsRequest request) {
        Mass mass = massService.getAvailableSeats(request);
        return ResponseEntity.ok(MapperUtil.map(mass, MassDto.class));
    }

    @GetMapping
    public ResponseEntity getMassesWithDate(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Mass> masses = massService.getMassesWithDate(date);
        return ResponseEntity.ok(masses.stream()
            .map(mass -> MapperUtil.map(mass, MassDto.class))
            .sorted(Comparator.comparing(MassDto::getTime))
            .collect(Collectors.toList()));
    }

}
