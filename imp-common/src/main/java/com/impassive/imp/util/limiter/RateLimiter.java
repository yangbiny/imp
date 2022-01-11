package com.impassive.imp.util.limiter;

import static java.lang.System.nanoTime;
import static java.util.concurrent.locks.LockSupport.parkNanos;

import com.impassive.imp.common.LimiterInfo;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RateLimiter implements Limiter {

  private final AtomicReference<State> state;

  public RateLimiter(LimiterInfo limiterInfo) {
    this.state = new AtomicReference<>(new State(limiterInfo));
  }

  @Override
  public void acquire(int acq) {
    //1. 计算要等待的时长
    long parkTime = calculateParkTime(acq);
    //2. 等待要等待的时间或者是超时时长
    parkWithTime(parkTime);
  }

  private void parkWithTime(long parkTime) {
    if (parkTime <= 0) {
      return;
    }
    //parkNanos(1);
  }

  private long calculateParkTime(int acq) {
    State state = this.state.get();
    long spendNanos = calculateSpendNanos(state);
    long nowCycle = calculateCycle(state, spendNanos);
    if (nowCycle == state.cycle && state.currentPermits >= acq) {
      State newState = new State(state.limiterInfo);
      boolean result = compareAndSwap(state, newState);
      return result ? 0 : 1000000000;
    }
    return 1000000000;
  }

  private long calculateCycle(State state, long spendNanos) {
    long periodNanos = state.periodNanos();
    return spendNanos / periodNanos;
  }

  private long calculateSpendNanos(State state) {
    return nanoTime() - state.startNano;
  }

  private boolean compareAndSwap(State oldState, State newState) {
    if (state.compareAndSet(oldState, newState)) {
      return true;
    }
    // 休眠一纳秒，减少冲突
    parkNanos(1);
    return false;
  }


  /**
   * 用于当前限流的状态
   */
  private static class State {

    private final LimiterInfo limiterInfo;

    private final long currentPermits;

    private final long startNano;

    private final long cycle;

    public State(LimiterInfo limiterInfo) {
      this.limiterInfo = limiterInfo;
      this.currentPermits = 0;
      this.cycle = 0;
      this.startNano = nanoTime();
    }

    public long periodNanos() {
      if (limiterInfo.getPeriodUnit() == TimeUnit.NANOSECONDS) {
        return limiterInfo.getPeriod();
      }
      return 1;
    }
  }
}
