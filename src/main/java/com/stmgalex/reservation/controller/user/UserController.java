package com.stmgalex.reservation.controller.user;

import com.stmgalex.reservation.dto.UserDto;
import com.stmgalex.reservation.service.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userService.register(userDto);
    }
}
