package com.ba.demo.core.utils;

import lombok.Getter;

@Getter
public class SpecificException extends RuntimeException {

  private final ErrorCode errorCode;
  private final MessagesKey messageKey;
  private final Object[] messageTokens;

  public SpecificException(ErrorCode errorCode, MessagesKey messageKey, Object... messageTokens) {
    assert messageKey.getNumTokens() <= 0
        || messageTokens != null && messageTokens.length == messageKey.getNumTokens();
    this.errorCode = errorCode;
    this.messageKey = messageKey;
    this.messageTokens = messageTokens;
  }

  public SpecificException(SpecificException exception, String message) {
    super(message);
    assert exception.messageKey.getNumTokens() <= 0
        || exception.messageTokens != null
            && exception.messageTokens.length == exception.messageKey.getNumTokens();
    this.errorCode = exception.errorCode;
    this.messageKey = exception.messageKey;
    this.messageTokens = exception.messageTokens;
  }
}
