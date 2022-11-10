package com.majorco.delay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxxiao
 **/
@Slf4j
public class TestTask extends DelayTask {

  public TestTask(String taskId, LocalDateTime invokeTime) {
    super(taskId, invokeTime);
  }

  @Override
  public void run() {
    System.out.println("延时任务" + this.getTaskId());
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
  }
}
