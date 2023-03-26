package com.srecko.reddit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

/**
 * The type Thymeleaf configuration.
 */
@Configuration
public class ThymeleafConfiguration {

  private ApplicationContext context;

  /**
   * Instantiates a new Thymeleaf configuration.
   *
   * @param context the context
   */
  @Autowired
  public ThymeleafConfiguration(ApplicationContext context) {
    this.context = context;
  }

  /**
   * Template resolver spring resource template resolver.
   *
   * @return the spring resource template resolver
   */
  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(context);
    templateResolver.setPrefix("resources/templates/");
    templateResolver.setSuffix(".html");
    return templateResolver;
  }

  /**
   * Template engine spring template engine.
   *
   * @return the spring template engine
   */
  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    return templateEngine;
  }

  /**
   * View resolver thymeleaf view resolver.
   *
   * @return the thymeleaf view resolver
   */
  @Bean
  public ThymeleafViewResolver viewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setApplicationContext(context);
    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setViewNames(new String[] {".html", ".xhtml"});
    return viewResolver;
  }
}
