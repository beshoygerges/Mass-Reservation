package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.MassReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MassReservationRepository extends JpaRepository<MassReservation, Integer> {
}
