package com.stmgalex.reservation;

import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.repository.MassRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@SpringBootApplication
public class MassReservationApplication implements CommandLineRunner {

    private final MassRepository massRepository;

    public static void main(String[] args) {
        SpringApplication.run(MassReservationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (massRepository.findAll().isEmpty()) {
            List<Mass> masses = new ArrayList<>();
            LocalDate date = LocalDate.now();
            for (int i = 0; i < 200; i++) {
                if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY) || date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                    Mass mass = new Mass();
                    mass.setDate(date);
                    mass.setTime(LocalTime.of(6, 0, 0));
                    masses.add(mass);
                    mass = new Mass();
                    mass.setDate(date);
                    mass.setTime(LocalTime.of(8, 0, 0));
                    masses.add(mass);
                } else if (date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
                    Mass mass = new Mass();
                    mass.setDate(date);
                    mass.setTime(LocalTime.of(8, 0, 0));
                    masses.add(mass);
                }
                date = date.plusDays(1);
            }
            massRepository.saveAll(masses);
        }
    }
}
