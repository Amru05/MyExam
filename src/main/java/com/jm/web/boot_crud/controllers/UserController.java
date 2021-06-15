package com.jm.web.boot_crud.controllers;

import com.jm.web.boot_crud.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/userInfo")
    public ResponseEntity<User> getUserInfo() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser.setPassword("");
        return ResponseEntity.ok().body(currentUser);
    }
}
