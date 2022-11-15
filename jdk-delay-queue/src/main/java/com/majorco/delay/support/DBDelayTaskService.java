package com.majorco.delay.support;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorco.delay.AbstractDelayTask;
import com.majorco.delay.AbstractTaskService;
import com.majorco.entity.DelayTask;
import com.majorco.exception.RepeatTask;
import com.majorco.service.DelayTaskRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xxxiao
 **/
@Component
@Slf4j
public class DBDelayTaskService extends AbstractTaskService {

  private final DelayTaskRepository delayTaskRepository;
  private final ObjectMapper classInfoObjectMapper;

  public DBDelayTaskService(DelayTaskRepository delayTaskRepository,
      ObjectMapper classInfoObjectMapper) {
    this.delayTaskRepository = delayTaskRepository;
    this.classInfoObjectMapper = classInfoObjectMapper;
  }

  @Override
  public boolean addDelayTaskRuntime(AbstractDelayTask delayTask) {
    if (!delayQueue.contains(delayTask)) {
      final DefaultIdentifierGenerator generator = new DefaultIdentifierGenerator();
      final Long nextId = generator.nextId(DelayTask.class);
      delayTask.setTaskId(nextId);
      String classInfo;
      try {
        classInfo = classInfoObjectMapper.writeValueAsString(delayTask);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
      final DelayTask task = DelayTask.builder()
          .id(nextId)
          .classInfo(classInfo)
          .taskDescription(delayTask.getTaskDescription())
          .taskName(delayTask.getTaskName())
          .isInvoked(false)
          .createTime(LocalDateTime.now())
          .invokeTime(delayTask.getInvokeTime()).build();
      final int isInsert = delayTaskRepository.getBaseMapper().insert(task);
      if (isInsert == 0) {
        log.error("insert to db failed,task: {}", delayTask);
      }
      delayTask.setTaskId(nextId);
      return delayQueue.add(delayTask) && isInsert > 0;
    } else {
      throw new RepeatTask("重复的任务");
    }
  }

  @Override
  public boolean addDelayTaskInit(AbstractDelayTask delayTask) {
    if (!delayQueue.contains(delayTask)) {
      return delayQueue.add(delayTask);
    }
    return false;
  }

  @Override
  public boolean removeDelayTask(AbstractDelayTask delayTask) {
    final DelayTask delete = DelayTask.builder()
        .updateTime(LocalDateTime.now())
        .taskName(delayTask.getTaskName())
        .isDelete(true).build();
    final LambdaUpdateWrapper<DelayTask> update = Wrappers.lambdaUpdate();
    update.eq(DelayTask::getId, delayTask.getTaskId());
    final int isUpdate = delayTaskRepository.getBaseMapper().update(delete, update);
    if (isUpdate == 0) {
      log.error("update to db failed,task: {}", delayTask);
    }
    return delayQueue.remove(delayTask) && isUpdate > 0;
  }

  @Override
  public void runAfter(AbstractDelayTask delayTask) {
    final DelayTask build = DelayTask.builder()
        .isInvoked(true)
        .updateTime(LocalDateTime.now())
        .build();
    final LambdaUpdateWrapper<DelayTask> update = Wrappers.lambdaUpdate();
    update.eq(DelayTask::getId, delayTask.getTaskId());
    delayTaskRepository.getBaseMapper().update(build, update);
  }

  @Override
  public void retry(AbstractDelayTask delayTask) {
    Integer retryCount = delayTask.getRetryCount();
    if (retryCount <= 3) {
      final LocalDateTime newInvokeTime = LocalDateTime.now().plus(10, ChronoUnit.SECONDS);
      delayTask.setInvokeTime(newInvokeTime);
      this.addDelayTaskInit(delayTask);
      log.error("retry count {},delayTask: {}", delayTask.getRetryCount(), delayTask.getTaskName());
      delayTask.setRetryCount(retryCount + 1);
    } else {
      final DelayTask build = DelayTask.builder().isFailed(true).build();
      final LambdaUpdateWrapper<DelayTask> update = Wrappers.lambdaUpdate();
      update.eq(DelayTask::getId, delayTask.getTaskId());
      delayTaskRepository.getBaseMapper().update(build, update);
      log.error("Failed to retry the delayed task for 3 times. Discard the execution");
    }
  }


}
