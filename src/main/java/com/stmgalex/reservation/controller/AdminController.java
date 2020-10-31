package com.stmgalex.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/rest/v1/admin", "/admin"})
public class AdminController {

    @GetMapping
    public String getHomeAdmin() {
        return "adminPage";
    }
}
