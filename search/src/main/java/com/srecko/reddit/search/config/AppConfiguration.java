package com.srecko.reddit.search.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
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
   * Encoder encoder.
   *
   * @param converters the converters
   * @return the encoder
   */
  @Bean
  public Encoder encoder(ObjectFactory<HttpMessageConverters> converters) {
    return new SpringFormEncoder(new SpringEncoder(converters));
  }
}
