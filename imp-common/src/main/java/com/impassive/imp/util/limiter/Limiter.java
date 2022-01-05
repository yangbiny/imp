package com.impassive.imp.util.limiter;

public interface Limiter {

  default void acquire() {
    acquire(1);
  }

  void acquire(int acq);

}
