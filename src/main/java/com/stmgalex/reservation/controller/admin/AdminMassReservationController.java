package com.stmgalex.reservation.controller.admin;

import com.stmgalex.reservation.dto.ReservationDto;
import com.stmgalex.reservation.entity.MassReservation;
import com.stmgalex.reservation.service.AdminService;
import com.stmgalex.reservation.util.MapperUtil;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping(value = {"/admin/masses/reservations"})
public class AdminMassReservationController {

  private final AdminService adminService;

  @GetMapping
  public String getReservations(Model model,
      @RequestParam(name = "page", defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "20") int size) {

    Sort sort = Sort.by("id").descending();

    PageRequest pageRequest = PageRequest.of(pageNumber, size, sort);

    Page<MassReservation> page = adminService.getMassReservations(pageRequest);

    model.addAttribute("reservations", page.getContent()
        .stream()
        .map(m -> MapperUtil.map(m, ReservationDto.class))
        .collect(Collectors.toList()));

    model.addAttribute("page", page);

    return "admin/reservations";
  }

  @GetMapping("{id}/disable")
  public String disableReservation(@PathVariable int id, HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    adminService.disableMassReservation(id);
    return "redirect:" + referer;
  }

  @GetMapping("{id}/enable")
  public String enableReservation(@PathVariable int id, HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    adminService.enableMassReservation(id);
    return "redirect:" + referer;
  }


}
