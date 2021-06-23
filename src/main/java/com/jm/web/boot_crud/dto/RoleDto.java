package com.jm.web.boot_crud.dto;

import com.jm.web.boot_crud.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleDto {
    private String name;

    public RoleDto() {
    }

    public RoleDto(String name) {
        this.name = name;
    }

    public RoleDto(Role role) {
        this.name = role.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
