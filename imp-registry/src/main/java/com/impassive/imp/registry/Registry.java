package com.impassive.imp.registry;

import com.impassive.imp.common.Url;

/** @author impassivey */
public interface Registry {

  /**
   * 服务注册。把url的信息，经过封装后，注册到注册中心
   *
   * @param url 需要注册的url的信息
   */
  void registry(Url url);
}
