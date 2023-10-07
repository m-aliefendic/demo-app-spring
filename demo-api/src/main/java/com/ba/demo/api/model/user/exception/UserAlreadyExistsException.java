package com.ba.demo.api.model.user.exception;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException() {
    super("User already exists.");
  }

  public UserAlreadyExistsException(String msg) {
    super(msg);
  }
}
