package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.MassReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MassReservationRepository extends JpaRepository<MassReservation, Integer> {

    Page<MassReservation> findAllByMass_Id(int massId, Pageable pageable);
}
