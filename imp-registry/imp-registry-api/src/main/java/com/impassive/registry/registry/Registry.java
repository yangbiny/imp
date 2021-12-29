package com.impassive.registry.registry;

import com.impassive.imp.Lifecycle;
import com.impassive.imp.common.Url;

/**
 * @author impassivey
 */
public interface Registry extends Lifecycle {

  /**
   * 服务注册。把url的信息，经过封装后，注册到注册中心
   *
   * @param url 需要注册的url的信息
   */
  void registry(Url url);

  /**
   * 取消服务注册。将对应的url的信息冲注册中心取消
   *
   * @param url url的信息
   */
  void unRegistry(Url url);

  /**
   * 订阅服务
   *
   * @param url URL的信息
   */
  void subscribe(Url url);

  /**
   * 取消订阅服务
   *
   * @param url URL的信息
   */
  void unSubscribe(Url url);
}
