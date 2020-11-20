package com.stmgalex.reservation;

import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.repository.EveningRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class MassReservationApplication implements CommandLineRunner {

    private final EveningRepository eveningRepository;

    public static void main(String[] args) {
        SpringApplication.run(MassReservationApplication.class, args);
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (eveningRepository.count() == 0) {
            List<Evening> evenings = new ArrayList<>();

            Evening evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 10));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 12));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 17));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 19));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 24));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 26));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2020, 12, 31));
            evenings.add(evening);

            evening = new Evening();
            evening.setTotalSeats(200);
            evening.setDate(LocalDate.of(2021, 1, 2));
            evenings.add(evening);

            eveningRepository.saveAll(evenings);
        }

    }
}
