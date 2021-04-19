package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.Mass;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MassRepository extends JpaRepository<Mass, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Mass> findByDateAndTimeAndEnabledIsTrue(LocalDate date, LocalTime time);

    @Modifying
    @Query("update Mass m set m.totalSeats = ?1, m.reservedSeats = m.reservedSeats+?2 where m.id = ?3")
    void updateMassById(Integer totalSeats, Integer reservedSeats, Integer id);

    Page<Mass> findAllByDateGreaterThanEqual(Pageable pageable, LocalDate date);

    Page<Mass> findAllByDateEquals(Pageable pageable, LocalDate date);

    List<Mass> findAllByDateEquals(LocalDate date);
}
