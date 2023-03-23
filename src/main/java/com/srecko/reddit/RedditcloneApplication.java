package com.srecko.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Redditclone application.
 *
 * @author Srecko Nikolic
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EntityScan(basePackages = {"com.srecko.reddit"})
@EnableScheduling
public class RedditcloneApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(RedditcloneApplication.class, args);
  }
}
