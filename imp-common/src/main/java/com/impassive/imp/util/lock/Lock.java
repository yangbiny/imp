package com.impassive.imp.util.lock;

import java.util.concurrent.TimeUnit;

public interface Lock {

  /**
   * 锁。
   *
   * @return true：获取锁成功
   */
  boolean lock();

  /**
   * 尝试获取锁，如果获取锁失败，则放弃
   *
   * @return true：获取锁成功
   */
  boolean tryLock();

  /**
   * 尝试获取锁。获取锁失败时，会等待指定的时间。如果指定时间后依旧获取失败，则获取锁失败
   *
   * @param time 等待的时间
   * @param timeUnit 等待时间的单位
   * @return true：获取锁成功
   */
  boolean tryLock(Long time, TimeUnit timeUnit);

  /**
   * 可中断式的获取锁。在获取锁阻塞时，可以响应其他线程的中断信号
   *
   * @return true：获取锁成功
   * @throws InterruptedException 响应中断
   */
  boolean lockWithInterruptibly() throws InterruptedException;
}
