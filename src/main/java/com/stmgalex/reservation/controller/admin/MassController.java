package com.stmgalex.reservation.controller.admin;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.entity.Mass;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping(value = {"/admin/masses"})
public class MassController {

    private final AdminService adminService;


    @GetMapping
    public String getMasses(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        Sort sort = Sort.by("id").ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Mass> massPage = adminService.getMasses(pageRequest, date);

        model.addAttribute("masses", massPage.getContent()
                .stream()
                .map(m -> MapperUtil.map(m, MassDto.class))
                .sorted(Comparator.comparing(MassDto::getId))
                .collect(Collectors.toList()));

        model.addAttribute("page", massPage);

        model.addAttribute("mass", new MassDto());

        return "admin/masses";
    }

    @GetMapping("/{id}/disable")
    public String disableMass(@PathVariable int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        adminService.disableMass(id);
        return "redirect:" + referer;
    }

    @GetMapping("/{id}/enable")
    public String enableMass(@PathVariable int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        adminService.enableMass(id);
        return "redirect:" + referer;
    }

    @PostMapping
    public String updateMass(@Valid @ModelAttribute("mass") MassDto mass, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        adminService.updateMass(mass);
        return "redirect:" + referer;
    }

    @GetMapping("/{id}/reservations")
    public void exportMassUsers(@PathVariable int id, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users.xlsx";
        response.setHeader(headerKey, headerValue);
        adminService.exportMassReservations(id, response);
    }

}
