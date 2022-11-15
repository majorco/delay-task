package com.majorco;

import com.majorco.delay.DelayTaskService;
import com.majorco.delay.SecondTask;
import com.majorco.delay.TestTask;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxxiao
 **/
@RestController
@Slf4j
public class Controller {

  @Resource
  private DelayTaskService delayTaskService;

  @GetMapping("/task")
  public void addTask() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final LocalDateTime parse = LocalDateTime.parse("2022-11-15 09:01:00", dateTimeFormatter);
    TestTask testTask = new TestTask("0.0.0.0.0.1", parse, "666");
    log.info(testTask.toString());
    delayTaskService.addDelayTask(testTask);
  }

  @GetMapping("/task2")
  public void addTask2() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final LocalDateTime parse = LocalDateTime.parse("2022-11-15 09:00:55", dateTimeFormatter);
    SecondTask testTask = new SecondTask("120.0.0.0.1", parse, "666", Arrays.asList("33", "44"));
    log.info(testTask.toString());
    delayTaskService.addDelayTask(testTask);
  }


}
