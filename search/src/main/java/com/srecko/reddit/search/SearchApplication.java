package com.srecko.reddit.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The type Search application.
 *
 * @author Srecko Nikolic
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableWebMvc
@EnableFeignClients
@EnableSpringDataWebSupport
public class SearchApplication {

  /**
  * The entry point of application.
  *
  * @param args the input arguments
  * */
  public static void main(String[] args) {
    SpringApplication.run(SearchApplication.class, args);
  }
}