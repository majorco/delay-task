package com.majorco.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorco.delay.AbstractDelayTask;
import com.majorco.delay.DelayTaskService;
import com.majorco.entity.DelayTask;
import com.majorco.service.DelayTaskRepository;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xxxiao
 **/
@Component
@Slf4j
public class DBPersistence extends DelayTaskService implements Persistence {


  public DBPersistence(DelayTaskRepository delayTaskRepository,
      ObjectMapper classInfoObjectMapper) {
    super(delayTaskRepository, classInfoObjectMapper);
  }

  @Override
  public boolean addDelayTaskInit(AbstractDelayTask delayTask) {
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

  @Override
  public boolean addDelayTask(AbstractDelayTask delayTask) {

    return false;
  }

  @Override
  public boolean removeDelayTask(AbstractDelayTask delayTask) {

    return false;
  }
}
