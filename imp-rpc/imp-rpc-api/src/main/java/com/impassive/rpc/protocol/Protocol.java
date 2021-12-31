package com.impassive.rpc.protocol;

import com.impassive.imp.Lifecycle;
import com.impassive.imp.common.Url;
import com.impassive.rpc.invoker.Invoker;

/**
 * 用来暴露服务到注册中心
 *
 * @author impassivey
 */
public interface Protocol extends Lifecycle {

  /**
   * 服务暴露的逻辑。会将invoke中的信息组装成 url 的方式，写入到注册中心
   *
   * @param invoker 需要暴露的服务信息
   * @param <T> 暴露的类型
   */
  <T> void export(Invoker<T> invoker);

  /**
   * 取消服务暴露。包括从注册中心移出注册的数据
   *
   * @param invoker 需要取消暴露的对象
   */
  <T> void unExport(Invoker<T> invoker);

  /**
   * 创建一个消费者的代理对象
   *
   * @param interfaceClass 代理对象的接口
   * @param url 代理对象的信息
   * @param <T> 代理对象的类型
   */
  <T> Invoker<T> refer(Class<T> interfaceClass, Url url);

  void unRefer(Url url);
}
