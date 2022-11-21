package com.majorco.delay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxxiao
 * 其他持久化任务的方式直接继承此类
 **/
@Slf4j
public abstract class AbstractTaskService extends AbstractPersistence {

  protected AbstractTaskService() {
    // 单线程无界队列
    final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(
        r -> {
          final Thread delayTaskThread = new Thread(r, "delayTaskThread");
          delayTaskThread.setDaemon(false);
          return delayTaskThread;
        });
    singleThreadExecutor.execute(() -> {
      for (; ; ) {
        // 当前线程如果被打上中断标记,程序无法正常运行,线程池中断
        if (Thread.currentThread().isInterrupted()) {
          return;
        }
        AbstractDelayTask delayTask = null;
        //waiting if necessary until an element with an expired delay is available
        try {
          delayTask = delayQueue.take();
          delayTask.run();
          //任务运行成功后执行的逻辑
          runAfter(delayTask);
          log.info("delayed task invoke success: {} ", delayTask.getTaskName());
        } catch (InterruptedException ex) {
          //中断标记
          Thread.currentThread().interrupt();
          ex.printStackTrace();
        } catch (Exception e) {
          //noinspection ConstantConditions
          log.error("delayed task invoke failed: {} ", delayTask.getTaskName());
          e.printStackTrace();
          retry(delayTask);
        }
      }
    });
  }

  /**
   * 用户程序重启加载任务后打印等待中的任务
   */
  public void printPendingTasks() {
    final String tasks = this.delayQueue.stream()
        .map(AbstractDelayTask::getTaskName).collect(Collectors.joining(","));
    log.info("Pending Tasks: {}", tasks);
  }

}
