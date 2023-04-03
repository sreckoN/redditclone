package com.srecko.reddit.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

  @Bean("testModelMapper")
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
