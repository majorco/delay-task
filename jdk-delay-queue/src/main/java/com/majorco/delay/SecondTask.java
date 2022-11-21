package com.majorco.delay;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxxiao
 * 测试任务
 **/
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SecondTask extends AbstractDelayTask {

  private String myname;
  private List<String> list;

  public SecondTask(String taskName, LocalDateTime invokeTime, String myname, List<String> list) {
    this.taskName = taskName;
    this.invokeTime = invokeTime;
    this.myname = myname;
    this.list = list;
  }

  @Override
  public void start() {
    log.info(Thread.currentThread().getName());

    log.info("延时任务: {}", taskName);
  }
}
