package com.jm.web.boot_crud.controllers;

import com.jm.web.boot_crud.dto.RoleDto;
import com.jm.web.boot_crud.dto.UserDto;
import com.jm.web.boot_crud.model.Role;
import com.jm.web.boot_crud.model.User;
import com.jm.web.boot_crud.service.RoleService;
import com.jm.web.boot_crud.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        try {
            UserDto user = new UserDto(userService.getUserById(id));
            user.setPassword("");
            return ResponseEntity.ok().body(user);
        } catch (ChangeSetPersister.NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/getAllUsers")
    public ResponseEntity<Iterable<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream().map(UserDto::new).collect(Collectors.toList());
        users.forEach((UserDto) -> UserDto.setPassword(""));
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/admin/getAllRoles")
    public ResponseEntity<Iterable<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles().stream().map(RoleDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(roles);
    }

    @PostMapping(value = "/admin/addUser")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDto userDto) {
        Set<Role> tmpRole = new HashSet<>();
        if (userDto.getRoles().isEmpty()) {
            tmpRole.add(roleService.getByName("USER"));
        } else {
            tmpRole = userDto.getRoles().stream().map(role -> roleService.getByName(role.getName())).collect(Collectors.toSet());
        }
        User user = new User(userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getAge(),
                userDto.getEmail(),
                userDto.getPassword(),
                tmpRole);
        userService.addUser(user);
        return new ResponseEntity<>(Collections.singletonMap("id", user.getId()), HttpStatus.OK);
    }

    @PatchMapping(value = "/admin/editUser")
    public ResponseEntity<?> editUser(@Validated @RequestBody UserDto userDto) {
        Set<Role> tmpRole = new HashSet<>();
        if (userDto.getRoles().isEmpty()) {
            tmpRole.add(roleService.getByName("USER"));
        } else {
            tmpRole = userDto.getRoles().stream().map(role -> roleService.getByName(role.getName())).collect(Collectors.toSet());
        }
        User user = new User(userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getAge(),
                userDto.getEmail(),
                userDto.getPassword(),
                tmpRole);
        user.setId(userDto.getId());
        try {
            userService.updateUser(user);
            return new ResponseEntity<>(Collections.singletonMap("id", user.getId()), HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.delUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

