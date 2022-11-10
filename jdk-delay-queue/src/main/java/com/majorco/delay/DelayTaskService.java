package com.majorco.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xxxiao
 **/
public class DelayTaskService {

  public final DelayQueue<DelayTask> delayQueue = new DelayQueue<>();

  @SuppressWarnings("InfiniteLoopStatement")
  public DelayTaskService() {
    final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(
        r -> {
          final Thread delayTaskThread = new Thread(r, "delayTaskThread");
          delayTaskThread.setDaemon(false);
          return delayTaskThread;
        });
    singleThreadExecutor.execute(() -> {
      for (; ; ) {
        try {
          final DelayTask delayTask = delayQueue.take();
          delayTask.run();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public boolean addDelayTask(DelayTask delayTask) {
    if (!delayQueue.contains(delayTask)) {
      return delayQueue.add(delayTask);
    }
    return false;
  }

  public boolean removeDelayTask(DelayTask delayTask) {
    return delayQueue.remove(delayTask);
  }

}
