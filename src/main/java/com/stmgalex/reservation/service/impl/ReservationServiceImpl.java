package com.stmgalex.reservation.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import com.stmgalex.reservation.dto.AvailableSeatsRequest;
import com.stmgalex.reservation.dto.CancelReservationRequest;
import com.stmgalex.reservation.dto.EveningReservationDisableRequest;
import com.stmgalex.reservation.dto.EveningReservationRequest;
import com.stmgalex.reservation.dto.MassReservationRequest;
import com.stmgalex.reservation.dto.ReservationResponse;
import com.stmgalex.reservation.dto.SearchReservationRequest;
import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.entity.EveningReservation;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.exception.EntityNotFoundException;
import com.stmgalex.reservation.exception.NoActiveReservationsException;
import com.stmgalex.reservation.exception.NoAvaialableSeatsException;
import com.stmgalex.reservation.exception.ReservationNotEnabledException;
import com.stmgalex.reservation.exception.UserNotFoundException;
import com.stmgalex.reservation.repository.EveningRepository;
import com.stmgalex.reservation.repository.EveningReservationRepository;
import com.stmgalex.reservation.repository.MassRepository;
import com.stmgalex.reservation.repository.MassReservationRepository;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.service.ReservationService;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private static int compareByMassDate(MassReservation e1, MassReservation e2) {
    return e2.getMass().getDate().compareTo(e1.getMass().getDate());
  }


  @Override
  public synchronized ReservationResponse reserve(final MassReservationRequest request) {

    Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

    User user = optionalUser.orElseThrow(() -> new RuntimeException("من فضلك قم بالتسجيل اولا"));

    Optional<Mass> optionalMass = massRepository
        .findByDateAndTime(request.getMassDate(), request.getMassTime());

    Mass mass = optionalMass
        .orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

    if (!mass.isEnabled()) {
      throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذا القداس");
    }

    if (!mass.haveSeats()) {
      throw new NoAvaialableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للقداس");
    }

    user.getMassReservations()
        .stream()
        .filter(massReservation -> massReservation.isActive()
            && massReservation.getMass().isEnabled()
            && massReservation.getMass().getDate().equals(mass.getDate()))
        .findFirst()
        .ifPresent(a -> {
          throw new RuntimeException("عفوا لقد قمت بحجز قداس في هذا اليوم من قبل");
        });

    if (!request.isYonanMass()) {
      user.getMassReservations()
          .stream()
          .filter(
              massReservation -> massReservation.isActive() && !massReservation.getMass().isYonan())
          .sorted(ReservationServiceImpl::compareByMassDate)
          .map(MassReservation::getMass)
          .filter(lastMass -> !isValidPeriod(mass, lastMass))
          .findFirst()
          .ifPresent(lastMass -> {
            throw new RuntimeException(
                "عفوا يجب ان تكون الفترة بين كل قداس والاخر مدة لا تقل عن 10 ايام");
          });
    }

    mass.reserveSeat();

    MassReservation massReservation = new MassReservation(user, mass);

    massReservation.setSeatNumber(mass.getReservedSeats());

    massReservation = massReservationRepository.save(massReservation);

    return new ReservationResponse(massReservation);
  }

  private boolean isValidPeriod(Mass mass, Mass lastMass) {
    long days = DAYS.between(mass.getDate(), lastMass.getDate());
    return days >= reservationPeriod || days <= -1 * reservationPeriod;
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
        .findByDateAndTime(request.getMassDate(), request.getMassTime());

    Mass mass = optionalMass
        .orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد قداسات في هذا التوقيت"));

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

    Evening evening = eveningOptional
        .orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد سهرة لهذا اليوم"));

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

    if (!evening.getDate().equals(LocalDate.of(2020, 12, 31))) {
      user.getEveningReservations()
          .stream()
          .filter(EveningReservation::isActive)
          .sorted(ReservationServiceImpl::compareByEveningDate)
          .map(EveningReservation::getEvening)
          .filter(lastEvening -> !isValidPeriod(evening, lastEvening))
          .findFirst()
          .ifPresent(lastEvening -> {
            throw new RuntimeException(
                "عفوا يجب ان تكون الفترة بين كل سهرة والاخري مدة لا تقل عن 10 ايام");
          });
    }

    evening.reserveSeat();

    EveningReservation reservation = new EveningReservation(user, evening);

    reservation = eveningReservationRepository.save(reservation);

    return new ReservationResponse(reservation);
  }

  @Transactional
  @Override
  public ReservationResponse cancelReservation(EveningReservationDisableRequest request) {
    Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

    User user = optionalUser
        .orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));

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

  private boolean isExceedMassIntervals(Mass mass, LocalDate massDate, boolean yonanMass) {

    long days = DAYS.between(mass.getDate(), massDate);

    if (yonanMass) {
      return days >= 1 || days <= -1;
    }

    return days >= reservationPeriod || days <= -1 * reservationPeriod;
  }
}
