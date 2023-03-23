package com.srecko.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EntityScan(basePackages = {"com.srecko.reddit"})
@EnableScheduling
public class RedditcloneApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedditcloneApplication.class, args);
  }
}
