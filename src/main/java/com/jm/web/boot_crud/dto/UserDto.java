package com.jm.web.boot_crud.dto;

import com.jm.web.boot_crud.model.User;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDto {

    private Long id;
    @Size(min = 3, message = "First Name field must be longer than 3")
    private String firstName;
    @Size(min = 3, message = "Last Name field must be longer than 3")
    private String lastName;
    @Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")
    private Integer age;
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 6, message = "Password must contain at least 6 symbols")
    private String password;
    private Set<RoleDto> roles;

    public UserDto() {
    }

    public UserDto(Long id, String firstName, String lastName, Integer age, String email, String password, Set<RoleDto> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles().stream().map(RoleDto::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }
}
