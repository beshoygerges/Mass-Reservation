package com.stmgalex.reservation;

import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class MassReservationApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MassReservationApplication.class, args);
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        for (User user : userRepository.findAll()) {
            try {
                user.setBirthdate(DateUtil.getBirthDate(user.getNationalId()));
                user.setAge(DateUtil.calculateAge(user.getBirthdate()));
            } catch (Exception e) {
                log.error("error for user " + user.getId());
            }
        }
    }
}
