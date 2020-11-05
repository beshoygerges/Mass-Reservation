package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.Statistics;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AdminService {

    Statistics getStatistics();

    List<MassDto> getMasses();

    Page<Mass> getMasses(Pageable pageRequest);

    void closeMass(int id);

    void openMass(int id);

    void exportMassReservations(int id, HttpServletResponse response) throws IOException;

    void updateMass(MassDto massDto);

    Page<Reservation> getReservations(PageRequest pageRequest);

    void closeReservation(int id);

    void openReservation(int id);
}
