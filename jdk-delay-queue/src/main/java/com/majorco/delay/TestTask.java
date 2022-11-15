package com.majorco.delay;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxxiao
 **/
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TestTask extends AbstractDelayTask {

  private String businessId;

  public TestTask(String taskName, LocalDateTime invokeTime, String businessId,
      String description) {
    this.taskName = taskName;
    this.invokeTime = invokeTime;
    this.businessId = businessId;
    this.taskDescription = description;
  }

  @Override
  public void start() {
    // select by busssId
    // shutdown bussid
//    System.out.println("延时任务" + this.getTaskName());
//    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
    log.info("任务执行{}", this);
    throw new RuntimeException("异常");
  }
}
