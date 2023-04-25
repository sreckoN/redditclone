package com.srecko.reddit.comments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
public class CommentsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommentsApplication.class, args);
  }

}
