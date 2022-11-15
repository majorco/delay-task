package com.majorco.delay;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxxiao
 **/
@Data
@EqualsAndHashCode
@Slf4j
public abstract class AbstractDelayTask implements Delayed, Runnable {

  protected String taskDescription;
  private Integer retryCount = 1;
  protected String taskName;
  protected LocalDateTime invokeTime;
  private Long taskId;

  /**
   * unit默认纳秒 这里精确到毫米
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
    if (!(delayed instanceof AbstractDelayTask)) {
      throw new IllegalArgumentException("args type incorrect");
    }
    AbstractDelayTask other = (AbstractDelayTask) delayed;
    return this.invokeTime.compareTo(other.getInvokeTime());
  }

  @Override
  public final void run() {
    this.start();
  }

  protected abstract void start();
}
