package com.ba.demo.dao.model;

import com.ba.demo.api.model.user.RoleDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class UserEntity {
    @Id
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private LocalDateTime registrationDate;
    private LocalDateTime updated;
    private String language;
    public String salt;

    @ManyToMany
    @JoinTable(
            name = "USER_ROLES", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key column in UserRoles table
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key column in UserRoles table
    )
    private Set<RoleEntity> roles;

}
