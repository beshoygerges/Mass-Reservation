package com.stmgalex.reservation.service;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.Statistics;
import com.stmgalex.reservation.entity.Mass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    Statistics getStatistics();

    List<MassDto> getMasses();

    Page<Mass> getMasses(Pageable pageRequest);
}
