package com.stmgalex.reservation.controller.admin;

import com.stmgalex.reservation.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping(value = {"/admin"})
public class DashboardController {

  private final AdminService adminService;

  @GetMapping({"", "/dashboard"})
  public String dashboard(Model model) {
    model.addAttribute("statistics", adminService.getStatistics());
    return "admin/dashboard";
  }
}
