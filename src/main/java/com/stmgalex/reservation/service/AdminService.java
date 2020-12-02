package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.Statistics;
import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.entity.EveningReservation;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public interface AdminService {

    Statistics getStatistics();

    Page<Mass> getMasses(Pageable pageable, LocalDate date);

    Page<Evening> getEvenings(Pageable pageable);

    void disableMass(int id);

    void enableMass(int id);

    void disableEvening(int id);

    void enableEvening(int id);

    void exportMassUsers(int id, HttpServletResponse response) throws IOException;

    void updateMass(MassDto massDto);

    Page<MassReservation> getMassReservations(Pageable pageable);

    Page<EveningReservation> getEveningReservations(Pageable pageable);

    void disableMassReservation(int id);

    void enableMassReservation(int id);

    void disableEveningReservation(int id);

    void enableEveningReservation(int id);

    void exportEveningUsers(int id, HttpServletResponse response) throws IOException;
}
