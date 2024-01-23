package com.test.personservice.shared.infrastructure.config.exceptions;

public class ObjectNotFoundException extends BaseException {

  public ObjectNotFoundException(String key, String message) {
    super(key, message, 404);
  }

}
