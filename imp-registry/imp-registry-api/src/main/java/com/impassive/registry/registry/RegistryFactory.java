package com.impassive.registry.registry;

import com.impassive.imp.Lifecycle;
import com.impassive.imp.common.Url;

/**
 * 用于生成对应的 注册对象
 *
 * @author impassivey
 */
public interface RegistryFactory extends Lifecycle {

  /**
   * 根据URL生成对应的 注册工厂
   *
   * @param url url
   * @return 注册工程
   */
  Registry getRegistry(Url url);
}
