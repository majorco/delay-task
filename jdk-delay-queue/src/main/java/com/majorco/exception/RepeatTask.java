package com.majorco.exception;

/**
 * @author xxxiao
 **/
public class RepeatTask extends RuntimeException {

  public RepeatTask(String msg) {
    super(msg);
  }
}
