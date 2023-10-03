package com.ba.demo.dao.repository;

import com.ba.demo.dao.model.token.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, UUID> {
    Stream<TokenEntity> findAllByUserId(UUID id);
}