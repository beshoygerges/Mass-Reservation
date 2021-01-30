package com.stmgalex.reservation;

import com.stmgalex.reservation.constants.Gender;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
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

    List<User> users = new ArrayList<>();

    userRepository.findAll().stream().forEach(user -> {
      int genderNumber = Integer.parseInt(user.getNationalId().substring(12, 13));
      if (genderNumber % 2 == 0) {
        user.setGender(Gender.FEMALE);
      } else {
        user.setGender(Gender.MALE);
      }
      users.add(user);
    });

    userRepository.saveAll(users);
  }
}
