package com.ba.demo.api.model.user;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private LocalDate updated;
    private String language;
    private Set<RoleDTO> roles = new HashSet<>();
}
