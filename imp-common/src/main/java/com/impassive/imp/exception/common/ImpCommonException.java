package com.impassive.imp.exception.common;

public class ImpCommonException extends RuntimeException {

  public ImpCommonException(String message) {
    super(message);
  }

  public ImpCommonException(Throwable cause) {
    super(cause);
  }
}
