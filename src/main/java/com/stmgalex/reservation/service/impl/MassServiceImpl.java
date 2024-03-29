package com.stmgalex.reservation.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.stmgalex.reservation.constants.Gender;
import com.stmgalex.reservation.dto.*;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.exception.*;
import com.stmgalex.reservation.repository.MassRepository;
import com.stmgalex.reservation.repository.MassReservationRepository;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.service.MassService;
import com.stmgalex.reservation.util.RangeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

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
    public synchronized ReservationResponse reserve(final MassReservationRequest request)
            throws IOException, WriterException {

        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());

        User user = optionalUser
                .orElseThrow(() -> new RuntimeException("من فضلك قم بالتسجيل اولا"));

        Optional<Mass> optionalMass = massRepository
                .findByDateAndTimeAndEnabledIsTrue(request.getMassDate(), request.getMassTime());

        Mass mass = optionalMass
                .orElseThrow(() -> new EntityNotFoundException("عفوا لا يوجد مناسبات في هذا التوقيت"));

        if (!mass.isEnabled()) {
            throw new ReservationNotEnabledException("عفوا لقد تم ايقاف الحجز على هذه المناسبة");
        }

        if (!mass.haveSeats(user.getGender())) {
            if (user.getGender() == Gender.MALE) {
                throw new NoAvailableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للرجال");
            }
            throw new NoAvailableSeatsException("عفوا لقد تم حجز جميع المقاعد المخصصة للسيدات");
        }

        if (ChronoUnit.HOURS
                .between(LocalDateTime.now(), LocalDateTime.of(mass.getDate(), mass.getTime())) < 12) {
            throw new RuntimeException("اخر ميعاد للحجز قبل المناسبة ب 12 ساعة");
        }

        if (mass.isSpecialEvent()) {
            user.getMassReservations()
                    .stream()
                    .filter(massReservation -> massReservation.isActive() && massReservation.getMass().isSpecialEvent())
                    .sorted(MassServiceImpl::compareByMassDate)
                    .map(MassReservation::getMass)
                    .filter(lastMass -> !isValidPeriod(mass, lastMass))
                    .findFirst()
                    .ifPresent(lastMass -> {
                        throw new RuntimeException(
                                "عفوا يجب ان تكون الفترة بين كل نهضة والاخري مدة لا تقل عن يومان");
                    });
        } else {

            user.getMassReservations()
                    .stream()
                    .filter(massReservation -> massReservation.isActive() && !massReservation.getMass().isSpecialEvent())
                    .sorted(MassServiceImpl::compareByMassDate)
                    .map(MassReservation::getMass)
                    .filter(lastMass -> !isValidPeriod(mass, lastMass))
                    .findFirst()
                    .ifPresent(lastMass -> {
                        throw new RuntimeException(
                                "عفوا يجب ان تكون الفترة بين كل قداس والاخر مدة لا تقل عن 14 يوم");
                    });

        }

        mass.reserveSeat(user.getGender());

        MassReservation massReservation = new MassReservation(user, mass);

        massReservation.setSeatNumber(mass.getReservedSeats());

        massReservation.setPlace(RangeUtil.getRangePlace(massReservation.getSeatNumber()));

        massReservation = massReservationRepository.save(massReservation);

        return generateQRReservationResponse(massReservation);
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

        Mass mass = massReservation.getMass();

        if (ChronoUnit.HOURS
                .between(LocalDateTime.now(), LocalDateTime.of(mass.getDate(), mass.getTime())) < 12) {
            throw new RuntimeException("اخر ميعاد لالغاء الحجز قبل القداس ب 12 ساعة");
        }

        massReservation.setActive(false);
        massReservation.getMass().releaseSeat(user.getGender());
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

    @Transactional
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

    @Override
    public ReservationResponse searchReservationQR(SearchReservationRequest request)
            throws IOException, WriterException {
        Optional<User> optionalUser = userRepository.findByNationalId(request.getNationalId());
        User user = optionalUser
                .orElseThrow(() -> new UserNotFoundException("عفوا هذا المستخدم غير موجود"));
        MassReservation massReservation = user
                .getMassReservation(request.getMassDate(), request.getMassTime());
        if (Objects.isNull(massReservation)) {
            throw new NoActiveReservationsException("عفوا لا يوجد حجوزات نشطة لك الان");
        }
        return generateQRReservationResponse(massReservation);
    }

    private ReservationResponse generateQRReservationResponse(MassReservation massReservation)
            throws WriterException, IOException {
        ReservationResponse response = new ReservationResponse(massReservation);
        StringBuilder qr = new StringBuilder("{\n");
        qr.append("  Name: " + response.getName() + ",\n");
        qr.append("  National Id: " + massReservation.getUser().getNationalId() + ",\n");
        qr.append("  Seat Number: " + response.getSeatNumber() + ",\n");
        qr.append("  Place: " + response.getPlace() + ",\n");
        qr.append("  Date: " + response.getMassDate().toString() + ",\n");
        qr.append("  Time: " + response.getMassTime().toString() + ",\n");
        qr.append("}");

        Hashtable hints = new Hashtable();

        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter
                .encode(qr.toString(), BarcodeFormat.QR_CODE, 250,
                        250, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        String qrString = new String(Base64.encodeBase64(pngOutputStream.toByteArray()),
                "UTF-8");

        response.setQr(qrString);

        return response;
    }

    private static int compareByMassDate(MassReservation e1, MassReservation e2) {
        return e2.getMass().getDate().compareTo(e1.getMass().getDate());
    }

    private boolean isValidPeriod(Mass mass, Mass lastMass) {
        long days = DAYS.between(mass.getDate(), lastMass.getDate());

        if (mass.isSpecialEvent()) {
            return days >= 2 || days <= -1 * 2;
        }

        return days >= reservationPeriod || days <= -1 * reservationPeriod;
    }

}
