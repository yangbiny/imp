package com.impassive.imp.util.limiter;

public interface Limiter {

  default boolean acquire() {
    return acquire(1);
  }

  boolean acquire(int acq);

}
