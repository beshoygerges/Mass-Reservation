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
//        List<Mass> masses = new ArrayList<>();
//
//        Mass mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 2));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 4));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 7));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 9));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 11));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 12));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 14));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 16));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 18));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 21));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 23));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 25));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 28));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        mass = new Mass();
//
//        mass.setDate(LocalDate.of(2021, 7, 30));
//        mass.setTime(LocalTime.of(8, 0, 0));
//        mass.setTotalSeats(50);
//        mass.setMenSeats(20);
//        mass.setWomenSeats(30);
//
//        masses.add(mass);
//
//        ///////////////////
//
//        massRepository.saveAll(masses);

    }
}
