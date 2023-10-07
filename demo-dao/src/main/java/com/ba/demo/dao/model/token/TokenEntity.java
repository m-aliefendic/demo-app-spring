package com.ba.demo.dao.model.token;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TOKEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
  @Id UUID id;
  UUID userId;
  LocalDateTime createdOn;
  LocalDateTime expiresOn;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TokenEntity that = (TokenEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(createdOn, that.createdOn);
  }
}
