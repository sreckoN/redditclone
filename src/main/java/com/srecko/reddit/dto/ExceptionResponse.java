package com.srecko.reddit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ExceptionResponse {

  private String message;
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "dd-MM-yyyy hh:mm:ss"
  )
  private LocalDateTime dateAndTime;

  public ExceptionResponse(String message, LocalDateTime dateAndTime) {
    this.message = message;
    this.dateAndTime = dateAndTime;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getDateAndTime() {
    return dateAndTime;
  }

  public void setDateAndTime(LocalDateTime dateAndTime) {
    this.dateAndTime = dateAndTime;
  }
}
