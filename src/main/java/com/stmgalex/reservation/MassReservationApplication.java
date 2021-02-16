package com.stmgalex.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class MassReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MassReservationApplication.class, args);
    }

}
