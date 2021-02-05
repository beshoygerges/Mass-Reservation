package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.Evening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EveningRepository extends JpaRepository<Evening, Integer> {

}
