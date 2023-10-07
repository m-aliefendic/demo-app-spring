package com.ba.demo.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"com.ba.demo.dao.model"})
public class JPAConfiguration {}
