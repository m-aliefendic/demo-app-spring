package com.ba.demo.dao.model.item;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ITEMS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
    @Id
    UUID id;
    UUID sellerId;
    String itemName;
    String description;
    Double price;
    LocalDateTime created;
    LocalDateTime updated;
}
