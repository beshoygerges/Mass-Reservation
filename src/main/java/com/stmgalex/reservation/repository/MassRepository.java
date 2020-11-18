package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.Mass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface MassRepository extends JpaRepository<Mass, Integer> {

    Optional<Mass> findByDateAndTime(LocalDate date, LocalTime time);

    @Modifying
    @Query("update Mass m set m.totalSeats = ?1, m.reservedSeats = m.reservedSeats+?2 where m.id = ?3")
    void updateMassById(Integer totalSeats, Integer reservedSeats, Integer id);

    Page<Mass> findAllByDateGreaterThanEqual(Pageable pageable, LocalDate date);

    Page<Mass> findAllByDateEquals(Pageable pageable, LocalDate date);
}
