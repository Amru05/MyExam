package com.jm.web.boot_crud.controllers;

import com.jm.web.boot_crud.dto.UserDto;
import com.jm.web.boot_crud.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/userInfo")
    public ResponseEntity<UserDto> getUserInfo() {
        UserDto currentUserDto = new UserDto((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        currentUserDto.setPassword("");
        return ResponseEntity.ok().body(currentUserDto);
    }
}
