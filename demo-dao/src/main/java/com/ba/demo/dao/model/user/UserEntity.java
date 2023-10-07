package com.ba.demo.dao.model.user;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class UserEntity {
  @Id private UUID id;
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
  private String salt;
  private UUID activationToken;

  @ManyToMany
  @JoinTable(
      name = "USER_ROLES", // Name of the join table
      joinColumns = @JoinColumn(name = "user_id"), // Foreign key column in UserRoles table
      inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key column in UserRoles table
      )
  private Set<RoleEntity> roles;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("USER"));
    return authorities;
  }
}
