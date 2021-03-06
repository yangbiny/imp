package com.impassive.rpc.proxy;

import com.impassive.rpc.invoker.Invoker;

/** @author impassivey */
public interface ProxyFactory {

  /**
   * 生成一个代理对象Invoker,最终代理对象包括了执行的对象信息
   *
   * @param ref 真正执行的对象
   * @param classType 对象的类型
   * @param <T> 返回的技术类型
   * @return 返回的代理对象
   */
  <T> Invoker<T> getInvoker(T ref, Class<T> classType);

  /**
   * 通过 invoker获取一个代理对象
   *
   * @param invoker invoker
   * @param <T> 代理对象的类型
   * @return 代理对象
   */
  <T> T proxy(Invoker<T> invoker);
}
