package com.srecko.reddit.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * The type Email configuration.
 *
 * @author Srecko Nikolic
 */
@Configuration
public class EmailConfiguration {

  private static final Logger logger = LogManager.getLogger(EmailConfiguration.class);

  /**
   * Thymeleaf template resolver.
   *
   * @return the template resolver
   */
  @Bean
  @Qualifier("thymeleafTemplateResolver")
  @Primary
  public ITemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("templates/mail-templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding("UTF-8");
    logger.debug("Created a bean of type: {}", ITemplateResolver.class);
    return templateResolver;
  }

  /**
   * Thymeleaf template engine spring template engine.
   *
   * @return the spring template engine
   */
  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(thymeleafTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(emailMessageSource());
    logger.debug("Created a bean of type: {}", SpringTemplateEngine.class);
    return templateEngine;
  }

  /**
   * Email message source resource bundle message source.
   *
   * @return the resource bundle message source
   */
  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("mailMessages");
    logger.debug("Created a bean of type: {}", ResourceBundleMessageSource.class);
    return messageSource;
  }
}
