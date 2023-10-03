package com.ba.demo.dao.repository;

import com.ba.demo.dao.model.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ItemRepository extends CrudRepository<ItemEntity, UUID> , CustomItemRepository {
    Page<ItemEntity> findAll(Pageable pageable);

}
