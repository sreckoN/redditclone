package com.srecko.reddit.subreddits.service.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class TestConfig {

  @Bean("testModelMapper")
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
