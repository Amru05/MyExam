package com.jm.web.boot_crud.service;

import com.jm.web.boot_crud.model.Role;
import com.jm.web.boot_crud.model.User;
import com.jm.web.boot_crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws ChangeSetPersister.NotFoundException {
        if (user.getPassword().isEmpty()) {
            user.setPassword(userRepository.findById(user.getId())
                    .orElseThrow(ChangeSetPersister.NotFoundException::new)
                    .getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User getUserById(Long id) throws ChangeSetPersister.NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    @Override
    @Transactional
    public User getUserByLogin(String login) {
        return userRepository.findByEmail(login);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user with credentials: " + s);
        }
        return user;
    }

    @Override
    @Transactional
    @PostConstruct
    public void initDB() {
        if (roleService.getByName("ADMIN") == null) {
            System.out.println("Let's create a role 'ADMIN' necessary for work");
            roleService.createRole("ADMIN");
        }
        if (roleService.getByName("USER") == null) {
            System.out.println("Let's create a role 'USER' necessary for work");
            roleService.createRole("USER");
        }
        if (userRepository.findByEmail("admin@mail.ru") == null) {
            System.out.println("Let's create admin-user necessary for work, with credentials: \n" +
                    "'admin@mail.ru' /  'password'");
            User user = new User("Admin",
                    "Adminus",
                    42,
                    "admin@mail.ru",
                    "password",
                    new HashSet<Role>() {{
                        add(roleService.getByName("ADMIN"));
                        add(roleService.getByName("USER"));
                    }});
            addUser(user);
        }
    }

}
