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

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class MassReservationApplication implements CommandLineRunner {

    private final MassRepository massRepository;

    public static void main(String[] args) {
        SpringApplication.run(MassReservationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<Mass> masses = new ArrayList<>();

        Mass mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 8));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 9));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 10));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 11));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 15));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 15));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 15));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 16));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 16));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 17));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 17));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 18));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 18));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 18));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 22));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 22));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 22));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 23));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 23));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 24));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 24));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 25));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 25));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 25));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 29));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 29));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 29));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 30));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 30));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 31));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 3, 31));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 1));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 1));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 1));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 5));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 5));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 5));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 6));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 6));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 7));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 8));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 8));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 8));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 12));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 12));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 12));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 13));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 13));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 14));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 14));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 15));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 15));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 15));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 19));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 19));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 19));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 20));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 20));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 22));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 22));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 22));
        mass.setTime(LocalTime.of(15, 0));
        masses.add(mass);

        massRepository.saveAll(masses);

    }
}
