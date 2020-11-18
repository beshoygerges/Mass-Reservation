package com.stmgalex.reservation.controller;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.ReservationDto;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.Reservation;
import com.stmgalex.reservation.service.AdminService;
import com.stmgalex.reservation.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping(value = {"/admin"})
public class AdminController {

    private final AdminService adminService;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("statistics", adminService.getStatistics());
        return "admin/dashboard";
    }

    @GetMapping({"/masses"})
    public String masses(Model model, @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        Sort sort = Sort.by("id").ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Mass> massPage = adminService.getMasses(pageRequest,date);

        model.addAttribute("masses", massPage.getContent()
                .stream()
                .map(m -> MapperUtil.map(m, MassDto.class))
                .sorted(Comparator.comparing(MassDto::getId))
                .collect(Collectors.toList()));

        model.addAttribute("page", massPage);

        model.addAttribute("mass", new MassDto());

        return "admin/masses";
    }

    @GetMapping("/masses/close/{id}")
    public String closeMass(@PathVariable int id) {
        adminService.closeMass(id);
        return "redirect:/admin/masses";
    }

    @GetMapping("/masses/open/{id}")
    public String openMass(@PathVariable int id) {
        adminService.openMass(id);
        return "redirect:/admin/masses";
    }

    @GetMapping("/masses/{id}/reservations")
    public void exportMassReservations(@PathVariable int id, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users.xlsx";
        response.setHeader(headerKey, headerValue);
        adminService.exportMassReservations(id, response);
    }

    @PostMapping("/masses")
    public String updateMass(@Valid @ModelAttribute("mass") MassDto mass) {
        adminService.updateMass(mass);
        return "redirect:/admin/masses";
    }

    @GetMapping({"/reservations"})
    public String reservations(Model model, @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                               @RequestParam(defaultValue = "10") int size) {

        Sort sort = Sort.by("id").descending();

        PageRequest pageRequest = PageRequest.of(pageNumber, size, sort);

        Page<Reservation> page = adminService.getReservations(pageRequest);

        model.addAttribute("reservations", page.getContent()
                .stream()
                .map(m -> MapperUtil.map(m, ReservationDto.class))
                .sorted(Comparator.comparing(ReservationDto::getId))
                .collect(Collectors.toList()));

        model.addAttribute("page", page);

        return "admin/reservations";
    }

    @GetMapping("/reservations/close/{id}")
    public String closeReservation(@PathVariable int id) {
        adminService.closeReservation(id);
        return "redirect:/admin/reservations";
    }

    @GetMapping("/reservations/open/{id}")
    public String openReservation(@PathVariable int id) {
        adminService.openReservation(id);
        return "redirect:/admin/reservations";
    }
}
