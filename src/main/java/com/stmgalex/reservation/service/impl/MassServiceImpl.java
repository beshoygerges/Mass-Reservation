package com.stmgalex.reservation.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.CancelReservationRequest;
import com.stmgalex.reservation.dto.MassReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.dto.SearchReservationRequest;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.exception.EntityNotFoundException;
import com.stmgalex.reservation.exception.NoActiveReservationsException;
import com.stmgalex.reservation.exception.NoAvailableSeatsException;
import com.stmgalex.reservation.exception.ReservationNotEnabledException;
import com.stmgalex.reservation.exception.UserNotFoundException;
import com.stmgalex.reservation.repository.MassRepository;
import com.stmgalex.reservation.repository.MassReservationRepository;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.service.MassService;
import com.stmgalex.reservation.util.RangeUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MassServiceImpl implements MassService {

    private final UserRepository userRepository;
    private final MassRepository massRepository;
    private final MassReservationRepository massReservationRepository;

    @Value("${mass.interval.days}")
    private int reservationPeriod;

    @Transactional
    @Override
    public synchronized ReservationResponse reserve(final MassReservationRequest request) {

        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser
            .orElseThrow(() -> new RuntimeException("من فضلك قم بالتسجيل اولا"));

        Optional<Mass> optionalMass = massRepository
            .findByDateAndTimeAndEnabledIsTrue(request.getMassDate(), request.getMassTime());

        Mass mass = optionalMass
            .orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new NoAvailableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }

        user.getMassReservations()
            .stream()
            .filter(massReservation -> massReservation.isActive()
                && massReservation.getMass().isEnabled()
                && massReservation.getMass().isYonan() == mass.isYonan()
                && massReservation.getMass().getDate().equals(mass.getDate()))
            .findFirst()
            .ifPresent(a -> {
                throw new RuntimeException("عفوا لقد قمت بحجز قداس في هذا اليوم من قبل");
            });

        if (mass.isYonan()) {
            long count = user.getMassReservations()
                .stream()
                .filter(massReservation -> massReservation.isActive() && massReservation.getMass()
                    .isYonan() && massReservation.getMass().getFeastWeek() == mass.getFeastWeek())
                .count();

            if (count + 1 > 2) {
                throw new RuntimeException(
                    "عفوا مسموح فقط بحضور بصختان مسائيتان فقط خلال اسبوع الالام");
            }

        } else {

            if (!mass.getDate().equals(LocalDate.of(2021, 4, 23))) {
                user.getMassReservations()
                    .stream()
                    .filter(
                        massReservation -> massReservation.isActive() && !massReservation.getMass()
                            .isYonan()
                            && !massReservation.getMass().getDate()
                            .equals(LocalDate.of(2021, 4, 23)))
                    .sorted(MassServiceImpl::compareByMassDate)
                    .map(MassReservation::getMass)
                    .filter(lastMass -> !isValidPeriod(mass, lastMass))
                    .findFirst()
                    .ifPresent(lastMass -> {
                        throw new RuntimeException(
                            "عفوا يجب ان تكون الفترة بين كل قداس والاخر مدة لا تقل عن 10 ايام");
                    });
            }
        }

        mass.reserveSeat();

        MassReservation massReservation = new MassReservation(user, mass);

        massReservation.setSeatNumber(mass.getReservedSeats());

        massReservation.setPlace(RangeUtil.getRangePlace(massReservation.getSeatNumber()));

        massReservation = massReservationRepository.save(massReservation);

        return new ReservationResponse(massReservation);
    }

    @Transactional
    @Override
    public ReservationResponse cancelReservation(final CancelReservationRequest request) {
        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser
            .orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));

        MassReservation massReservation = user
            .getMassReservation(request.getMassDate(), request.getMassTime());

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
        User user = optionalUser
            .orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));
        MassReservation massReservation = user
            .getMassReservation(request.getMassDate(), request.getMassTime());
        if (Objects.isNull(massReservation)) {
            throw new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان");
        }
        return new ReservationResponse(massReservation);
    }

    @Override
    public Mass getAvailableSeats(AvailableSeatsRequest request) {

        Optional<Mass> optionalMass = massRepository
            .findByDateAndTimeAndEnabledIsTrue(request.getMassDate(), request.getMassTime());

        Mass mass = optionalMass
            .orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new NoAvailableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }
        return mass;
    }

    @Override
    public List<Mass> getMassesWithDate(LocalDate date) {
        List<Mass> masses = massRepository.findAllByDateEquals(date);
        if (masses.isEmpty()) {
            throw new RuntimeException("عفوا لا توجد قداسات في هذا اليوم");
        }
        return masses;
    }

    private static int compareByMassDate(MassReservation e1, MassReservation e2) {
        return e2.getMass().getDate().compareTo(e1.getMass().getDate());
    }

    private boolean isValidPeriod(Mass mass, Mass lastMass) {
        long days = DAYS.between(mass.getDate(), lastMass.getDate());
        return days >= reservationPeriod || days <= -1 * reservationPeriod;
    }

}
