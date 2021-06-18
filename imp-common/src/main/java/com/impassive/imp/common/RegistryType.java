package com.impassive.imp.common;

import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

/** @author impassivey */
public enum RegistryType {

  /** Zookeeper作为注册中心 */
  ZOOKEEPER;

  @Nullable
  public static RegistryType of(String registryType){
    for (RegistryType value : RegistryType.values()) {
      if (StringUtils.equalsIgnoreCase(value.name(),registryType)){
        return value;
      }
    }
    return null;
  }
}
