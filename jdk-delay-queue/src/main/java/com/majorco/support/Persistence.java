package com.majorco.support;

import com.majorco.delay.AbstractDelayTask;

/**
 * @author xxxiao
 **/
public interface Persistence {

  /**
   * 用于程序重启从持久化程序加载
   */
  boolean addDelayTaskInit(AbstractDelayTask delayTask);

  /**
   * 运行时添加任务
   */
  boolean addDelayTask(AbstractDelayTask delayTask);

  /**
   * 运行时删除任务
   */
  boolean removeDelayTask(AbstractDelayTask delayTask);

}
