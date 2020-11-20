package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.*;
import com.stmgalex.reservation.entity.*;
import com.stmgalex.reservation.exception.*;
import com.stmgalex.reservation.repository.*;
import com.stmgalex.reservation.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
    private int nextMassAfter;

    @Override
    public synchronized ReservationResponse reserve(final MassReservationRequest request) {

        List<User> users = userRepository.findByNationalIdOrName(request.getNationalId(), request.getName());

        User user = null;

        if (users.isEmpty())
            user = createUser(request);

        else if (users.size() == 1)
            user = users.get(0);

        else
            throw new RuntimeException("برجاء التأكد من الاسم والرقم القومي");

        Mass mass = user.getLastActiveMass();

        if (Objects.nonNull(mass) && !isExceedMassIntervals(mass, request.getMassDate())) {
            throw new MassIntervalNotExceededException("    عفوا يمكنك الحجز مجددا من يوم " + mass.getDate().plusDays(nextMassAfter) + " ");
        }

        Optional<Mass> optionalMass = massRepository.findByDateAndTime(request.getMassDate(), request.getMassTime());

        mass = optionalMass.orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
        }

        if (!mass.haveSeats()) {
            throw new NoAvaialableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
        }

        user.setName(request.getName());

        user.setBirthdate(DateUtil.getBirthDate(request.getNationalId()));

        user.setAge(DateUtil.calculateAge(user.getBirthdate()));

        mass.reserveSeat();

        userRepository.save(user);

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
    public ReservationResponse reserve(EveningReservationRequest request) {
        List<User> users = userRepository.findByNationalIdOrName(request.getNationalId(), request.getName());

        User user = null;

        if (users.isEmpty())
            user = createUser(request);

        else if (users.size() == 1)
            user = users.get(0);

        else
            throw new RuntimeException("برجاء التأكد من الاسم والرقم القومي");

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

        user.setName(request.getName());

        user.setBirthdate(DateUtil.getBirthDate(request.getNationalId()));

        user.setAge(DateUtil.calculateAge(user.getBirthdate()));

        evening.reserveSeat();

        userRepository.save(user);

        EveningReservation reservation = new EveningReservation(user, evening);

        reservation = eveningReservationRepository.save(reservation);

        return new ReservationResponse(reservation);
    }

    private User createUser(EveningReservationRequest request) {
        User user = new User();
        user.setMobileNumber(request.getMobileNumber());
        user.setName(request.getName());
        user.setNationalId(request.getNationalId());
        return user;
    }

    private boolean isExceedMassIntervals(Mass mass, LocalDate massDate) {
        return DAYS.between(mass.getDate(), massDate) >= nextMassAfter;
    }

    private User createUser(MassReservationRequest request) {
        User user = new User();
        user.setMobileNumber(request.getMobileNumber());
        user.setName(request.getName());
        user.setNationalId(request.getNationalId());
        return user;
    }
}
