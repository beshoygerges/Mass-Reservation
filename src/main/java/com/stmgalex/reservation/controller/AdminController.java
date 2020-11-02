package com.stmgalex.reservation.controller;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.service.AdminService;
import com.stmgalex.reservation.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping(value = {"/admin"})
public class AdminController {

    private final AdminService adminService;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("statistics", adminService.getStatistics());
        return "admin/dashboard";
    }

    @GetMapping({"/masses"})
    public String masses(Model model, @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {

        Sort sort = Sort.by("id").ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Mass> massPage = adminService.getMasses(pageRequest);


        model.addAttribute("masses", massPage.getContent()
                .stream()
                .map(m -> MapperUtil.map(m, MassDto.class))
                .sorted(Comparator.comparing(MassDto::getId))
                .collect(Collectors.toList()));

        model.addAttribute("currentPage", massPage);

        model.addAttribute("totalPages", massPage.getTotalPages());

        model.addAttribute("totalItems", massPage.getTotalElements());

        return "admin/masses";
    }
}
