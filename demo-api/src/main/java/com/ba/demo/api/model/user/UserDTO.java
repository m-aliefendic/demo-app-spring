package com.ba.demo.api.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(allowSetters = true, value = {"password"})
public class UserDTO {
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
    private Integer language;
    private Set<RoleDTO> roles;
}
