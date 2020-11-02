package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.Statistics;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.Reservation;
import com.stmgalex.reservation.repository.MassRepository;
import com.stmgalex.reservation.repository.ReservationRepository;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final MassRepository massRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    public Statistics getStatistics() {
        List<Mass> masses = massRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();

        int totalAttendants = masses.stream()
                .filter(mass -> mass.getDate().isBefore(LocalDate.now().plusDays(1)))
                .map(Mass::getReservedSeats)
                .collect(Collectors.summingInt(Integer::intValue));

        int totalAvailable = masses.stream()
                .filter(mass -> mass.getDate().isBefore(LocalDate.now().plusDays(1)))
                .map(Mass::getTotalSeats)
                .collect(Collectors.summingInt(Integer::intValue));


        return Statistics.builder()
                .masses(masses.size())
                .users(userRepository.count())
                .reservations(reservations.size())
                .approvedReservations(reservations.stream().filter(Reservation::isActive).count())
                .canceledReservations(reservations.stream().filter(r -> !r.isActive()).count())
                .completedMasses(masses.stream().filter(m -> !m.haveSeats()).count())
                .attendancePercentage(totalAttendants * 1.0 / totalAvailable * 100d)
                .build();
    }

    @Override
    public List<MassDto> getMasses() {
        return massRepository.findAll()
                .stream()
                .map(m -> MapperUtil.map(m, MassDto.class))
                .sorted(Comparator.comparing(MassDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Mass> getMasses(Pageable pageRequest) {
        return massRepository.findAll(pageRequest);
    }
}
