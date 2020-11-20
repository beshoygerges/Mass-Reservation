package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.entity.Mass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface EveningRepository extends JpaRepository<Evening, Integer> {

}
