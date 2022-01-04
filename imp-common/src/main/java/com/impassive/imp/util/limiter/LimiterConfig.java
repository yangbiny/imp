package com.impassive.imp.util.limiter;

import java.util.concurrent.TimeUnit;
import lombok.Data;

@Data
public class LimiterConfig {

  private Boolean enable;

  private Long permits = 1L;

  private Integer time = 1;

  private TimeUnit timeUnit = TimeUnit.SECONDS;

}
