package com.stmgalex.reservation.controller.admin;

import com.stmgalex.reservation.dto.EveningDto;
import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.service.AdminService;
import com.stmgalex.reservation.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping(value = {"/admin/evenings"})
public class EveningController {

    private final AdminService adminService;

    @GetMapping
    public String getMasses(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        Sort sort = Sort.by("id").ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Evening> eveningPage = adminService.getEvenings(pageRequest);

        model.addAttribute("evenings", eveningPage.getContent()
                .stream()
                .map(m -> MapperUtil.map(m, EveningDto.class))
                .sorted(Comparator.comparing(EveningDto::getId))
                .collect(Collectors.toList()));

        model.addAttribute("page", eveningPage);

        return "admin/evenings";
    }

    @GetMapping("/{id}/disable")
    public String disableEvening(@PathVariable int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        adminService.disableEvening(id);
        return "redirect:" + referer;
    }

    @GetMapping("/{id}/enable")
    public String enableEvening(@PathVariable int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        adminService.enableEvening(id);
        return "redirect:" + referer;
    }

}
