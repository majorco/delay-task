package com.majorco;

import com.majorco.delay.SecondTask;
import com.majorco.delay.TestTask;
import com.majorco.delay.support.DBDelayTaskService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
  private DBDelayTaskService delayTaskService;
  private Integer num = 1;

  @GetMapping("/task")
  public void addTask() {
    final LocalDateTime plus = LocalDateTime.now().plus(10, ChronoUnit.SECONDS);
    TestTask testTask = new TestTask("延时任务" + num, plus,
        "延时任务" + num, "延时任务" + num);
    log.info(testTask.toString());
    num = num + 1;
    delayTaskService.addDelayTaskRuntime(testTask);
    //delayTaskService.removeDelayTask(testTask);
  }

  @GetMapping("/task2")
  public void addTask2() {
    final LocalDateTime plus = LocalDateTime.now().plus(10, ChronoUnit.SECONDS);
    SecondTask testTask = new SecondTask("120.0.0.0.1", plus, "666", Arrays.asList("33", "44"));
    log.info(testTask.toString());
    delayTaskService.addDelayTaskRuntime(testTask);
  }


}
