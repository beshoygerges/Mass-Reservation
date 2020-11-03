package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.ReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.Reservation;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.exception.*;
import com.stmgalex.reservation.repository.MassRepository;
import com.stmgalex.reservation.repository.ReservationRepository;
import com.stmgalex.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final MassRepository massRepository;
    private final ReservationRepository reservationRepository;

    @Value("${mass.interval.days}")
    private int nextMassAfter;

    @Override
    public synchronized ReservationResponse reserve(final ReservationRequest request) {

        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser.orElse(createUser(request));

        Mass mass = user.getLastActiveMass();

        if (Objects.nonNull(mass) && !isExceedMassIntervals(mass)) {
            throw new MassIntervalNotExceededException("    عفوا يمكنك الحجز مجددا من يوم " + mass.getDate().plusDays(nextMassAfter) + " ");
        }

        Optional<Mass> optionalMass = massRepository.findByDateAndTime(request.getMassDate(), request.getMassTime());

        mass = optionalMass.orElseThrow(() -> new MassNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new MassNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new MassHaveNoSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }

        mass.reserveSeat();

        userRepository.save(user);

        Reservation reservation = new Reservation(user, mass);

        reservation.setSeatNumber(mass.getReservedSeats());

        reservation = reservationRepository.save(reservation);

        return new ReservationResponse(reservation);
    }

    @Transactional
    @Override
    public ReservationResponse cancelReservation(String nationalId) {
        Optional<User> optionalUser = userRepository.findByNationalId(nationalId);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));
        Reservation reservation = user.getLastReservation();
        if (Objects.isNull(reservation)) {
            throw new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان");
        }
        reservation.setActive(false);
        reservation.getMass().releaseSeat();
        return new ReservationResponse(reservation);
    }

    @Override
    public ReservationResponse searchReservation(String nationalId) {
        Optional<User> optionalUser = userRepository.findByNationalId(nationalId);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));
        Reservation reservation = user.getLastReservation();
        if (Objects.isNull(reservation)) {
            throw new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان");
        }
        return new ReservationResponse(reservation);
    }

    @Override
    public Mass getAvailableSeats(AvailableSeatsRequest request) {

        Optional<Mass> optionalMass = massRepository.findByDateAndTime(request.getMassDate(), request.getMassTime());

        Mass mass = optionalMass.orElseThrow(() -> new MassNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new MassNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new MassHaveNoSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }
        return mass;
    }

    private boolean isExceedMassIntervals(Mass mass) {
        return DAYS.between(mass.getDate(), LocalDate.now()) >= nextMassAfter;
    }

    private User createUser(ReservationRequest request) {
        User user = new User();
        user.setMobileNumber(request.getMobileNumber());
        user.setName(request.getName());
        user.setNationalId(request.getNationalId());
        return user;
    }
}
