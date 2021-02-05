package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.EveningReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EveningReservationRepository extends JpaRepository<EveningReservation, Integer> {

}
