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

  /**
   * 任务描述
   */
  protected String taskDescription;
  /**
   * 任务名字
   */
  protected String taskName;
  /**
   * 任务执行时间
   */
  protected LocalDateTime invokeTime;
  /**
   * 重试计数
   */
  private Integer retryCount = 1;
  /**
   * 任务id 默认由mp雪花算法生成,调用setTaskId无效
   */
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
