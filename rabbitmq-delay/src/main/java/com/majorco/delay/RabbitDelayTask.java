package com.majorco.delay;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xxxiao
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitDelayTask implements Serializable, Runnable {

  private static final long serialVersionUID = -6881959415222743107L;
  private String taskId;

  @Override
  public void run() {

  }
}
