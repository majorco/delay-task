package com.majorco.delay;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xxxiao
 **/
@Data
@EqualsAndHashCode
public abstract class DelayTask implements Delayed, Runnable {

  private String taskId;
  private LocalDateTime invokeTime;

  protected DelayTask(String taskId, LocalDateTime invokeTime) {
    this.taskId = taskId;
    this.invokeTime = invokeTime;
  }

  /**
   * unit默认纳秒级别 支持毫米级别延迟
   *
   * @param unit the time unit
   * @return long
   */
  @Override
  public long getDelay(TimeUnit unit) {
    return invokeTime.atOffset(ZoneOffset.of("+8")).toInstant().toEpochMilli()
        - System.currentTimeMillis();
  }

  @Override
  public int compareTo(Delayed delayed) {
    if (!(delayed instanceof DelayTask)) {
      throw new IllegalArgumentException("args type incorrect");
    }
    DelayTask other = (DelayTask) delayed;
    return this.invokeTime.compareTo(other.getInvokeTime());
  }
}
