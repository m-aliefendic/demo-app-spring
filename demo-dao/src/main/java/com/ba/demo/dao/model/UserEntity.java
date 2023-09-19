package com.ba.demo.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TEST")
@Getter
@Setter
public class UserEntity {
    @Id
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

    @ManyToMany
    @JoinTable(
            name = "USER_ROLES", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key column in UserRoles table
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key column in UserRoles table
    )
    private Set<RoleEntity> roles = new HashSet<>();

}
