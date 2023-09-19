package com.ba.demo.dao.repository;

import com.ba.demo.dao.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Page<UserEntity> findAllByCompanyNameContainingIgnoreCase(String name, Pageable pageable);
    Page<UserEntity> findAll(Pageable pageable);

}
