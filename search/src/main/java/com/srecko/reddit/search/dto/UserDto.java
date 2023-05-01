package com.srecko.reddit.search.dto;

import java.util.Date;
import java.util.Objects;

/**
 * The type User dto.
 *
 * @author Srecko Nikolic
 */
public class UserDto {

  private Long id;

  private String username;

  private String country;

  private Date registrationDate;

  /**
   * Instantiates a new User dto.
   */
  public UserDto() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto userDto = (UserDto) o;
    return id.equals(userDto.id) && username.equals(userDto.username) && Objects.equals(
        country, userDto.country) && registrationDate.equals(userDto.registrationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, country, registrationDate);
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets country.
   *
   * @param country the country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Gets registration date.
   *
   * @return the registration date
   */
  public Date getRegistrationDate() {
    return registrationDate;
  }

  /**
   * Sets registration date.
   *
   * @param registrationDate the registration date
   */
  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}
