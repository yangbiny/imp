package com.impassive.imp.common;

import com.impassive.imp.util.limiter.LimiterConfig;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

@Getter
public class LimiterInfo {

  private final Boolean enable;

  private Long permits;

  private Integer time;

  private TimeUnit timeUnit;

  public LimiterInfo(LimiterConfig limiterConfig) {
    if (limiterConfig == null) {
      this.enable = Boolean.FALSE;
      return;
    }
    this.enable = limiterConfig.getEnable();
    this.permits = limiterConfig.getPermits();
    this.time = limiterConfig.getTime();
    this.timeUnit = limiterConfig.getTimeUnit();
  }
}
