package com.srecko.reddit.posts.exception.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * The type Exception response.
 *
 * @author Srecko Nikolic
 */
public class ExceptionResponse {

  private String message;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "dd-MM-yyyy hh:mm:ss"
  )
  private LocalDateTime dateAndTime;

  /**
   * Instantiates a new Exception response.
   *
   * @param message     the message
   * @param dateAndTime the date and time
   */
  public ExceptionResponse(String message, LocalDateTime dateAndTime) {
    this.message = message;
    this.dateAndTime = dateAndTime;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets date and time.
   *
   * @return the date and time
   */
  public LocalDateTime getDateAndTime() {
    return dateAndTime;
  }

  /**
   * Sets date and time.
   *
   * @param dateAndTime the date and time
   */
  public void setDateAndTime(LocalDateTime dateAndTime) {
    this.dateAndTime = dateAndTime;
  }
}
