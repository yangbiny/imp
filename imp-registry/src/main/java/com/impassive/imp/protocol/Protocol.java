package com.impassive.imp.protocol;

/**
 * 用来暴露服务到注册中心
 *
 * @author impassivey
 */
public interface Protocol {

  /**
   * 服务暴露的逻辑。会将invoke中的信息组装成 url 的方式，写入到注册中心
   *
   * @param invoker 需要暴露的服务信息
   * @param <T> 暴露的类型
   */
  <T> void export(Invoker<T> invoker);
}
