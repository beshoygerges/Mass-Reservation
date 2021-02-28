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

        Mass  mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 21));
        mass.setTime(LocalTime.of(10, 0));
        masses.add(mass);

        mass = new Mass();
        mass.setDate(LocalDate.of(2021, 4, 21));
        mass.setTime(LocalTime.of(13, 0));
        masses.add(mass);


        massRepository.saveAll(masses);

    }
}
