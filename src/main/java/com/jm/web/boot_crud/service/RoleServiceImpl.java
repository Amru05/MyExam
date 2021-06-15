package com.jm.web.boot_crud.service;

import com.jm.web.boot_crud.model.Role;
import com.jm.web.boot_crud.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        List<Role> roleList = new ArrayList<>();
        roleRepository.findAll().forEach(roleList::add);
        return roleList;
    }

    @Override
    @Transactional
    public Role getByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role getById(Long id) throws ChangeSetPersister.NotFoundException {
        return roleRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    @Override
    @Transactional
    public void createRole(String name) {
        roleRepository.save(new Role(name));
    }
}
