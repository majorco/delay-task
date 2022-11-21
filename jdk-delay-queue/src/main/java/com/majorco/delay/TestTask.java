package com.majorco.delay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.DelayQueue;
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
/*
 * 根据 sensorId 更新任务
 */
@EqualsAndHashCode(callSuper = false, of = "sensorId")
@ToString(callSuper = true)
@Data
public class TestTask extends AbstractDelayTask {

  private String sensorId;

  public TestTask(String taskName, LocalDateTime invokeTime, String businessId,
      String description) {
    this.taskName = taskName;
    this.invokeTime = invokeTime;
    this.sensorId = businessId;
    this.taskDescription = description;
  }

  public static void main(String[] args) {
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final LocalDateTime parse = LocalDateTime.parse("2022-11-19 22:22:22", dateTimeFormatter);
    final DelayQueue<AbstractDelayTask> abstractDelayTasks = new DelayQueue<>();
    final TestTask testTask = new TestTask();
    testTask.setSensorId("2022");
    testTask.setInvokeTime(parse);
    final TestTask testTask2 = new TestTask();
    testTask2.setSensorId("2022");
    final LocalDateTime parse2 = LocalDateTime.parse("2022-11-20 22:22:22", dateTimeFormatter);
    testTask2.setInvokeTime(parse2);

    abstractDelayTasks.add(testTask);
    abstractDelayTasks.forEach(System.err::println);
    abstractDelayTasks.remove(testTask2);
    System.err.println("------------------------------");
    abstractDelayTasks.add(testTask2);
    abstractDelayTasks.forEach(System.err::println);

  }

  @Override
  public void start() {
    // select by busssId
    // shutdown bussid
//    System.out.println("延时任务" + this.getTaskName());
//    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
    log.info("任务执行{}", this);
  }
}
