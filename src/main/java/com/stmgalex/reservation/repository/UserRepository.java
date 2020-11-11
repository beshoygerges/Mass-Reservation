package com.stmgalex.reservation.repository;

import com.stmgalex.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNationalId(String nationalId);

    List<User> findByNationalIdOrName(String nationalId, String name);
}
