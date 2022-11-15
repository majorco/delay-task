package com.majorco.delay;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorco.entity.DelayTask;
import com.majorco.service.DelayTaskRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xxxiao
 **/
@Component
@Slf4j
public class DelayTaskService {

  public final DelayQueue<AbstractDelayTask> delayQueue = new DelayQueue<>();
  private final DelayTaskRepository delayTaskRepository;
  private final ObjectMapper classInfoObjectMapper;


  @SuppressWarnings({"InfiniteLoopStatement"})
  public DelayTaskService(DelayTaskRepository delayTaskRepository,
      ObjectMapper classInfoObjectMapper) {
    this.delayTaskRepository = delayTaskRepository;
    this.classInfoObjectMapper = classInfoObjectMapper;
    final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(
        r -> {
          final Thread delayTaskThread = new Thread(r, "singDelayedTaskThread");
          delayTaskThread.setDaemon(false);
          return delayTaskThread;
        });
    singleThreadExecutor.execute(() -> {
      for (; ; ) {
        AbstractDelayTask delayTask = null;
        try {
          //waiting if necessary until an element with an expired delay is available
          delayTask = delayQueue.take();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.error("thread take() Interrupted");
          e.printStackTrace();
        }
        try {
          if (Objects.nonNull(delayTask)) {
            delayTask.run();
            final DelayTask build = DelayTask.builder().isInvoked(true)
                .updateTime(LocalDateTime.now())
                .build();
            final LambdaUpdateWrapper<DelayTask> update = Wrappers.lambdaUpdate();
            update.eq(DelayTask::getTaskName, delayTask.getTaskName());
            delayTaskRepository.getBaseMapper().update(build, update);
            log.info("delayed task: {} invoke success", delayTask.getTaskName());
          }
        } catch (Exception e) {
          //noinspection ConstantConditions
          log.error("delayed task: {} invoke failed", delayTask.getTaskName());
          e.printStackTrace();
          retry(this.delayQueue, delayTask);
        }
      }
    });
  }

  @SuppressWarnings("unused")
  @Transactional(rollbackFor = Exception.class)
  public boolean addDelayTask(AbstractDelayTask delayTask) {
    if (!delayQueue.contains(delayTask)) {
      String classInfo;
      try {
        classInfo = classInfoObjectMapper.writeValueAsString(delayTask);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
      final DelayTask task = DelayTask.builder().classInfo(classInfo)
          .taskName(delayTask.getTaskName()).isInvoked(false)
          .createTime(LocalDateTime.now()).invokeTime(delayTask.getInvokeTime()).build();
      final int isInsert = delayTaskRepository.getBaseMapper().insert(task);
      if (isInsert == 0) {
        log.error("insert to db failed,task: {}", delayTask);
      }
      return delayQueue.add(delayTask) && isInsert > 0;
    }
    return false;
  }

  @SuppressWarnings("unused")
  public boolean addDelayTask(AbstractDelayTask delayTask, boolean init) {
    if (!delayQueue.contains(delayTask)) {
      return delayQueue.add(delayTask);
    }
    return false;
  }

  @SuppressWarnings("unused")
  @Transactional(rollbackFor = Exception.class)
  public boolean removeDelayTask(AbstractDelayTask delayTask) {
    final DelayTask delete = DelayTask.builder().updateTime(LocalDateTime.now())
        .taskName(delayTask.getTaskName()).isDelete(true).build();
    final LambdaUpdateWrapper<DelayTask> update = Wrappers.lambdaUpdate();
    update.eq(DelayTask::getTaskName, delayTask.getTaskName());
    final int isUpdate = delayTaskRepository.getBaseMapper().update(delete, update);
    if (isUpdate == 0) {
      log.error("update to db failed,task: {}", delayTask);
    }
    return delayQueue.remove(delayTask) && isUpdate > 0;
  }

  public void printPendingTasks() {
    final String tasks = this.delayQueue.stream()
        .map(AbstractDelayTask::toString).collect(Collectors.joining(","));
    log.info("Pending Tasks: {}", tasks);
  }
}
