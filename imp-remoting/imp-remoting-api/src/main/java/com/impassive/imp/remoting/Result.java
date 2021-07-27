package com.impassive.imp.remoting;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** @author impassivey */
public interface Result extends Request {

  /**
   * 获取返回的结果
   *
   * @return 结果信息
   */
  Object getResult();

  /**
   * 设置返回值的信息
   *
   * @param object 返回的对象
   */
  void setResult(Object object);

  <U> CompletableFuture<U> thenApply(Function<Result, ? extends U> object);
}
