package com.ba.demo.dao.repository;

import com.ba.demo.dao.model.user.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Page<UserEntity> findAll(Pageable pageable);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByEmailAndActivationToken(String email, UUID activationToken);
}
