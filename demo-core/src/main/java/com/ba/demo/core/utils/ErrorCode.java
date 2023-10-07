package com.ba.demo.core.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorCode {
  public static final ErrorCode RESOURCE_NOT_FOUND =
      new ErrorCode(HttpStatus.NOT_FOUND, "Resource not found");
  public static final ErrorCode CONFLICT =
      new ErrorCode(HttpStatus.CONFLICT, "Conflicting Request");
  public static final ErrorCode USERNAME_CONFLICT =
      new ErrorCode(HttpStatus.CONFLICT, "Username Conflict");
  public static final ErrorCode FORBIDDEN = new ErrorCode(HttpStatus.FORBIDDEN, "Forbidden action");
  public static final ErrorCode BAD_REQUEST = new ErrorCode(HttpStatus.BAD_REQUEST, "Bad Request");
  public static final ErrorCode UNAUTHORIZED =
      new ErrorCode(HttpStatus.UNAUTHORIZED, "Unauthorized Request");
  public static final ErrorCode EXPIRED_TOKEN =
      new ErrorCode(HttpStatus.UNAUTHORIZED, "Expired Token");
  public static final ErrorCode STALE_DATA = new ErrorCode(HttpStatus.CONFLICT, "Stale Data");
  public static final ErrorCode UN_PROCESSABLE =
      new ErrorCode(HttpStatus.UNPROCESSABLE_ENTITY, "Un-processable Request");
  public static final ErrorCode SERVER_ERROR =
      new ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
  public static final ErrorCode NOT_FOUND = new ErrorCode(HttpStatus.NOT_FOUND, "Not Found");

  private final HttpStatus httpStatus;
  private final String message;

  protected ErrorCode(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
