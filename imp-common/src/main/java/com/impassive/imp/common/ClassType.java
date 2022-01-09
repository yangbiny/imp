package com.impassive.imp.common;

import lombok.Getter;

/**
 * 类的类型。需要配置在resource的相关文件中。
 */
@Getter
public enum ClassType {

  /**
   * 服务发现相关
   */
  discover("discover"),

  /**
   * 过滤器相关
   */
  filter("filter"),

  /**
   * 路由相关
   */
  routing("routing"),

  /**
   * 扩展点相关。由于每个扩展点的名字都不同，所以这里是空的
   */
  extension("");

  private final String path;

  ClassType(String path) {
    this.path = path;
  }
}
