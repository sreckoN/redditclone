package com.srecko.reddit.subreddits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
public class SubredditsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SubredditsApplication.class, args);
  }

}
