package com.srecko.reddit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web configuration.
 *
 * @author Srecko Nikolic
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfiguration implements WebMvcConfigurer {

}