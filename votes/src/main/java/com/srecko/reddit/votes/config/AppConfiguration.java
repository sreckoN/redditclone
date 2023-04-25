package com.srecko.reddit.votes.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type App configuration.
 *
 * @author Srecko Nikolic
 */
@Configuration
public class AppConfiguration {

  /**
   * Model mapper model mapper.
   *
   * @return the model mapper
   */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
