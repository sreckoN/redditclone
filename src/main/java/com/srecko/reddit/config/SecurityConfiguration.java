package com.srecko.reddit.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.srecko.reddit.filters.AuthenticationFilter;
import com.srecko.reddit.filters.JwtAuthenticationFilter;
import com.srecko.reddit.jwt.JwtConfig;
import com.srecko.reddit.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Security configuration.
 *
 * @author Srecko Nikolic
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserDetailsService userDetailsService;
  private final AppLogoutHandler logoutHandler;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final RefreshTokenService refreshTokenService;

  /**
   * Instantiates a new Security configuration.
   *
   * @param jwtAuthenticationFilter      the jwt authentication filter
   * @param userDetailsService           the user details service
   * @param logoutHandler                the logout handler
   * @param authenticationManagerBuilder the authentication manager builder
   * @param refreshTokenService          the refresh token service
   */
  @Autowired
  public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
      UserDetailsService userDetailsService, AppLogoutHandler logoutHandler,
      AuthenticationManagerBuilder authenticationManagerBuilder,
      RefreshTokenService refreshTokenService) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
    this.logoutHandler = logoutHandler;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.refreshTokenService = refreshTokenService;
  }

  /**
   * Filter chain security filter chain.
   *
   * @param http the http
   * @return the security filter chain
   * @throws Exception the exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    String[] staticResources = {
        "/css/**",
        "/images/**",
        "/fonts/**",
        "/scripts/**",
    };
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(new JwtConfig(),
        authenticationManagerBuilder.getOrBuild(), refreshTokenService);
    authenticationFilter.setFilterProcessesUrl("/api/auth/authenticate");
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(STATELESS)
        .and()
        .authorizeHttpRequests()
        .requestMatchers(staticResources).permitAll()
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/search/**").permitAll()
        .requestMatchers("/api").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginProcessingUrl("/api/auth/authenticate")
        .and()
        .authenticationProvider(daoAuthenticationProvider())
        .addFilter(authenticationFilter)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .addLogoutHandler(logoutHandler)
            .logoutUrl("/api/auth/logout")
            .logoutSuccessUrl("/login.html"));
    return http.build();
  }

  /**
   * Password encoder password encoder.
   *
   * @return the password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Dao authentication provider.
   *
   * @return the authentication provider
   */
  @Bean
  public AuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setUserDetailsService(userDetailsService);
    return authProvider;
  }
}