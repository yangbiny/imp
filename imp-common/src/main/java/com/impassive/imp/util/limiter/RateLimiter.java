package com.impassive.imp.util.limiter;

public class RateLimiter implements Limiter {

  @Override
  public boolean acquire(int acq) {
    return false;
  }
}
