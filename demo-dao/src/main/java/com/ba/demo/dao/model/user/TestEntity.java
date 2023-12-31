package com.ba.demo.dao.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TEST")
@Getter
@Setter
public class TestEntity {
  private static final long serialVersionID = 1L;

  @Id private long id;

  @Column(name = "TEST_NAME")
  private String testName;
}
