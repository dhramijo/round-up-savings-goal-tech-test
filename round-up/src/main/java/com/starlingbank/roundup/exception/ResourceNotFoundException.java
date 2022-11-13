package com.starlingbank.roundup.exception;

public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -435742587845152856L;

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
