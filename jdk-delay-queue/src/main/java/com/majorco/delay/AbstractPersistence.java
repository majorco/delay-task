package com.majorco.delay;

import com.majorco.delay.support.Persistence;
import java.util.concurrent.DelayQueue;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxxiao
 **/
@Slf4j
public abstract class AbstractPersistence implements Persistence {

  protected final DelayQueue<AbstractDelayTask> delayQueue = new DelayQueue<>();

}
