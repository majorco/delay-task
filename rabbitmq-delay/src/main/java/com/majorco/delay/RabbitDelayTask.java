package com.majorco.delay;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author xxxiao
 **/
@Data
@Builder
public abstract class RabbitDelayTask implements Serializable, Runnable {

  private static final long serialVersionUID = -6881959415222743107L;
  private String taskId;
}
