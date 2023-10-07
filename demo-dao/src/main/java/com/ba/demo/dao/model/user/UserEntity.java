package com.ba.demo.dao.model.user;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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
  @UpdateTimestamp private LocalDateTime updated;
  private Integer language;
  private String salt;
  private UUID activationToken;
  private LocalDateTime activationExpiresOn;
  private Boolean active = Boolean.FALSE;

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
