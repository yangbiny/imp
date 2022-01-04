package com.impassive.imp.util.lock;

public interface ReadWriteLock {

  /**
   * 读锁
   *
   * @return 读锁
   */
  Lock readLock();

  /**
   * 写锁
   *
   * @return 写锁
   */
  Lock writeLock();

}
