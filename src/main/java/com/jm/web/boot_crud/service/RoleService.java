package com.jm.web.boot_crud.service;

import com.jm.web.boot_crud.model.Role;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getById(Long id) throws ChangeSetPersister.NotFoundException;

    Role getByName(String name);

    void createRole(String name);
}
