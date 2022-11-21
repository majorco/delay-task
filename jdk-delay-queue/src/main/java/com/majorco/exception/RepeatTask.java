package com.majorco.exception;

/**
 * @author xxxiao
 * 重复添加的任务
 **/
public class RepeatTask extends RuntimeException {

  public RepeatTask(String msg) {
    super(msg);
  }
}
