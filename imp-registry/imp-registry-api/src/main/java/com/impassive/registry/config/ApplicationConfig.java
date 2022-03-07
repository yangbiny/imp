package com.impassive.registry.config;

import lombok.Getter;

/**
 * 用于配置项目的信息
 *
 * @author impassivey
 */
@Getter
public class ApplicationConfig {

  private String applicationName;

  private String serialization = "json";

  public void setApplicationName(String applicationName){
    this.applicationName = applicationName;
  }

  public void setSerialization(String serialization) {
    this.serialization = serialization;
  }
}
