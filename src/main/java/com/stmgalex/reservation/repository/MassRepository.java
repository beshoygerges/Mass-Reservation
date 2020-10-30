package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.Mass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface MassRepository extends JpaRepository<Mass, Integer> {
    Optional<Mass> findByDateAndTime(LocalDate date,  LocalTime time);
}
