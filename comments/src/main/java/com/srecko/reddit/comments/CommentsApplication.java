package com.srecko.reddit.comments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The type Comments application.
 *
 * @author Srecko Nikolic
 */
@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
public class CommentsApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(CommentsApplication.class, args);
  }

}
