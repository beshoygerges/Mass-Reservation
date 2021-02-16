package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.Statistics;
import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.entity.EveningReservation;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface AdminService {

  Statistics getStatistics();

  Page<Mass> getMasses(Pageable pageable, LocalDate date);

  void disableMass(int id);

  void enableMass(int id);

  void exportMassUsers(int id, HttpServletResponse response) throws IOException;

  void updateMass(MassDto massDto);

  Page<MassReservation> getMassReservations(Pageable pageable);

  void disableMassReservation(int id);

  void enableMassReservation(int id);

  Page<MassReservation> getMassReservations(int id, PageRequest pageRequest);
}
