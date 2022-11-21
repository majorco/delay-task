package com.majorco;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.majorco.delay.AbstractDelayTask;
import com.majorco.delay.support.DBDelayTaskService;
import com.majorco.entity.DelayTask;
import com.majorco.service.DelayTaskRepository;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xxxiao
 * 项目重启扫描持久化的任务
 **/
@Component
public class AddUnInvokedTaskRunner implements ApplicationRunner {

  private final ObjectMapper classInfoObjectMapper;
  private final DelayTaskRepository delayTaskRepository;
  private final DBDelayTaskService delayTaskService;

  public AddUnInvokedTaskRunner(ObjectMapper classInfoObjectMapper,
      DelayTaskRepository delayTaskRepository,
      DBDelayTaskService delayTaskService) {
    this.classInfoObjectMapper = classInfoObjectMapper;
    this.delayTaskRepository = delayTaskRepository;
    this.delayTaskService = delayTaskService;
  }

  /**
   * 程序启动扫描未执行的任务
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
    final List<DelayTask> delayTasks = delayTaskRepository.getBaseMapper()
        .selectList(Wrappers.lambdaQuery(DelayTask.class)
            .eq(DelayTask::getIsInvoked, false)
            .eq(DelayTask::getIsDelete, false)
            .eq(DelayTask::getIsFailed, false));
    for (DelayTask delayTask : delayTasks) {
      final String classInfo = delayTask.getClassInfo();
      //反序列化json成task类
      final Object aClass = classInfoObjectMapper.readValue(classInfo,
          TypeFactory.defaultInstance().constructType(Object.class));
      delayTaskService.addDelayTaskInit((AbstractDelayTask) aClass);
    }
    //初始化完成打印当前队列中的任务,如果任务执行时间小于当前时间,会被立马执行,打印不准确
    delayTaskService.printPendingTasks();
  }
}










