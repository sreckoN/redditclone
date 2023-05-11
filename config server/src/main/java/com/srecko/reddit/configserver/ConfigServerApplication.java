package com.srecko.reddit.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * The type ConfigServer application.
 *
 * @author Srecko Nikolic
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }

}
