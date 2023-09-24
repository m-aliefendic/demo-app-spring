package com.ba.demo.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class RoleEntity {
    @Id
    private Long id;
    private String roleName;
    private String description;
    @ManyToMany(mappedBy = "roles") // Refers to the 'roles' property in UserDTO
    private Set<UserEntity> users = new HashSet<>();
}