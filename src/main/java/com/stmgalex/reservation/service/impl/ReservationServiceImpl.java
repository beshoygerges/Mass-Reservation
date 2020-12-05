package com.stmgalex.reservation.service.impl;

import com.stmgalex.reservation.dto.*;
import com.stmgalex.reservation.entity.*;
import com.stmgalex.reservation.exception.*;
import com.stmgalex.reservation.repository.*;
import com.stmgalex.reservation.service.ReservationService;
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
    private final EveningRepository eveningRepository;
    private final MassReservationRepository massReservationRepository;
    private final EveningReservationRepository eveningReservationRepository;

    @Value("${mass.interval.days}")
    private int reservationPeriod;

    private static int compareByEveningDate(EveningReservation e1, EveningReservation e2) {
        return e2.getEvening().getDate().compareTo(e1.getEvening().getDate());
    }

    @Override
    public synchronized ReservationResponse reserve(final MassReservationRequest request) {

        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());


        User user = optionalUser.orElseThrow(() -> new RuntimeException("من فضلك قم بالتسجيل اولا"));


        Mass mass = user.getLastActiveMass();

        if (Objects.nonNull(mass) && !isExceedMassIntervals(mass, request.getMassDate())) {
            throw new MassIntervalNotExceededException("    عفوا يمكنك الحجز مجددا من يوم " + mass.getDate().plusDays(reservationPeriod) + " ");
        }

        Optional<Mass> optionalMass = massRepository.findByDateAndTime(request.getMassDate(), request.getMassTime());

        mass = optionalMass.orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new NoAvaialableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }

        mass.reserveSeat();

        MassReservation massReservation = new MassReservation(user, mass);

        massReservation.setSeatNumber(mass.getReservedSeats());

        massReservation = massReservationRepository.save(massReservation);

        return new ReservationResponse(massReservation);
    }

    @Transactional
    @Override
    public ReservationResponse cancelReservation(final CancelReservationRequest request) {
        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));

        MassReservation massReservation = user.getMassReservation(request.getMassDate(), request.getMassTime());

        if (Objects.isNull(massReservation)) {
            throw new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان");
        }
        massReservation.setActive(false);
        massReservation.getMass().releaseSeat();
        return new ReservationResponse(massReservation);
    }

    @Override
    public ReservationResponse searchReservation(final SearchReservationRequest request) {
        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));
        MassReservation massReservation = user.getMassReservation(request.getMassDate(), request.getMassTime());
        if (Objects.isNull(massReservation)) {
            throw new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان");
        }
        return new ReservationResponse(massReservation);
    }

    @Override
    public Mass getAvailableSeats(AvailableSeatsRequest request) {

        Optional<Mass> optionalMass = massRepository.findByDateAndTime(request.getMassDate(), request.getMassTime());

        Mass mass = optionalMass.orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new NoAvaialableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }
        return mass;
    }

    @Override
    public synchronized ReservationResponse reserve(EveningReservationRequest request) {

        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser.orElseThrow(() -> new RuntimeException("من فضلك قم بالتسجيل اولا"));

        Optional<Evening> eveningOptional = eveningRepository.findById(request.getEveningId());

        Evening evening = eveningOptional.orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد سهرة لهذا اليوم"));

        if (!evening.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذه السهرة");
        }

        if (!evening.haveSeats()) {
            throw new NoAvaialableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للسهرة");
        }

        user.getEveningReservations()
                .stream()
                .filter(er -> er.isActive() && er.getEvening().equals(evening))
                .findFirst()
                .ifPresent(a -> {
                    throw new RuntimeException("عفوا لقد قمت بحجز هذه السهرة من قبل");
                });

        if (!evening.getDate().equals(LocalDate.of(2020, 12, 31)))
            user.getEveningReservations()
                    .stream()
                    .filter(EveningReservation::isActive)
                    .sorted(ReservationServiceImpl::compareByEveningDate)
                    .map(EveningReservation::getEvening)
                    .filter(lastEvening -> !isValidPeriod(evening, lastEvening))
                    .findFirst()
                    .ifPresent(lastEvening -> {
                        throw new RuntimeException("عفوا يجب ان تكون الفترة بين كل سهرة والاخري مدة لا تقل عن 10 ايام");
                    });


        evening.reserveSeat();

        EveningReservation reservation = new EveningReservation(user, evening);

        reservation = eveningReservationRepository.save(reservation);

        return new ReservationResponse(reservation);
    }

    @Transactional
    @Override
    public ReservationResponse cancelReservation(EveningReservationDisableRequest request) {
        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));

        EveningReservation eveningReservation = user.getEveningReservations()
                .stream()
                .filter(e -> e.getEvening().getId() == request.getEveningId())
                .findFirst()
                .orElseThrow(() -> new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان"));

        eveningReservation.setActive(false);

        eveningReservation.getEvening().releaseSeat();

        return new ReservationResponse(eveningReservation);
    }

    private boolean isValidPeriod(Evening evening, Evening lastEvening) {
        long days = DAYS.between(evening.getDate(), lastEvening.getDate());
        return days >= reservationPeriod || days <= -1 * reservationPeriod;
    }

    private boolean isExceedMassIntervals(Mass mass, LocalDate massDate) {
        long days = DAYS.between(mass.getDate(), massDate);
        return days >= reservationPeriod || days <= -1 * reservationPeriod;
    }
}
