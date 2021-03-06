package com.impassive.imp.util.lock;

import java.util.concurrent.TimeUnit;

public class ReentrantReadWriteLock implements ReadWriteLock {

  private final ReadLock readLock;


  private final WriteLock writeLock;

  public ReentrantReadWriteLock() {
    this.readLock = new ReadLock();
    this.writeLock = new WriteLock();
  }

  @Override
  public Lock readLock() {
    return readLock;
  }

  @Override
  public Lock writeLock() {
    return writeLock;
  }


  private static class ReadLock implements Lock {

    @Override
    public boolean lock() {
      return false;
    }

    @Override
    public boolean tryLock() {
      return false;
    }

    @Override
    public boolean tryLock(Long time, TimeUnit timeUnit) {
      return false;
    }

    @Override
    public boolean lockWithInterruptibly() throws InterruptedException {
      return false;
    }
  }

  private static class WriteLock implements Lock {

    @Override
    public boolean lock() {
      return false;
    }

    @Override
    public boolean tryLock() {
      return false;
    }

    @Override
    public boolean tryLock(Long time, TimeUnit timeUnit) {
      return false;
    }

    @Override
    public boolean lockWithInterruptibly() throws InterruptedException {
      return false;
    }
  }
}
