package com.jm.web.boot_crud.controllers;

import com.jm.web.boot_crud.model.Role;
import com.jm.web.boot_crud.model.User;
import com.jm.web.boot_crud.service.RoleService;
import com.jm.web.boot_crud.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            user.setPassword("");
            return ResponseEntity.ok().body(user);
        } catch (ChangeSetPersister.NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/getAllUsers")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach((User) -> User.setPassword(""));
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/admin/getAllRoles")
    public ResponseEntity<Iterable<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok().body(roles);
    }

    @PostMapping("/admin/addUser")
    public ResponseEntity<Long> createUser(@RequestBody User user) {
        Set<Role> tmpRole = new HashSet<>();
        if (user.getRoles().isEmpty()) {
            tmpRole.add(roleService.getByName("USER"));
        } else {
            tmpRole = user.getRoles().stream().map(role -> roleService.getByName(role.getName())).collect(Collectors.toSet());
        }
        user.setRoles(tmpRole);
        userService.addUser(user);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.delUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/admin/editUser")
    public ResponseEntity<?> editUser(@RequestBody User user) {
        Set<Role> tmpRole = new HashSet<>();
        if (user.getRoles().isEmpty()) {
            tmpRole.add(roleService.getByName("USER"));
        } else {
            tmpRole = user.getRoles().stream().map(role -> roleService.getByName(role.getName())).collect(Collectors.toSet());
        }
        user.setRoles(tmpRole);
        try {
            userService.updateUser(user);
            return new ResponseEntity<>(user.getId(), HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

