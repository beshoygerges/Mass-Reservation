package com.stmgalex.reservation;

import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.repository.MassRepository;
import java.time.LocalDate;
import java.time.LocalTime;
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

  private final MassRepository massRepository;

  public static void main(String[] args) {
    SpringApplication.run(MassReservationApplication.class, args);
  }

  @Transactional
  @Override
  public void run(String... args) throws Exception {
    List<Mass> masses = new ArrayList<>();

    Mass mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 22));
    mass.setTime(LocalTime.of(11, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 22));
    mass.setTime(LocalTime.of(13, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 22));
    mass.setTime(LocalTime.of(15, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 23));
    mass.setTime(LocalTime.of(11, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 23));
    mass.setTime(LocalTime.of(13, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 23));
    mass.setTime(LocalTime.of(15, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 24));
    mass.setTime(LocalTime.of(11, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 24));
    mass.setTime(LocalTime.of(13, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    mass = new Mass();
    mass.setDate(LocalDate.of(2021, 2, 24));
    mass.setTime(LocalTime.of(15, 00));
    mass.setTotalSeats(150);
    mass.setYonan(true);

    masses.add(mass);

    massRepository.saveAll(masses);

  }
}
