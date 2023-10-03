package com.ba.demo.dao.repository;

import com.ba.demo.api.model.user.exception.UserNotFoundException;
import com.ba.demo.dao.model.user.UserEntity;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    Page<UserEntity> findAll(Pageable pageable);
    Optional<UserEntity> findByEmail(String email);

}
