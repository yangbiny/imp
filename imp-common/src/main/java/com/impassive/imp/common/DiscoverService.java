package com.impassive.imp.common;

import lombok.Data;

@Data
public class DiscoverService {

  /**
   * 协议的信息
   */
  private String protocol;

  private String host;

  private Integer port;

  private String className;

  private String groupName;

}
