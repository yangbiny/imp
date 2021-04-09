package com.impassive.imp.protocol;

import lombok.Data;

/**
 * 封装了最终的所有的参数信息
 *
 * @author impassivey
 */
@Data
public class Url {

  private String applicationName;

  private Class<?> classType;

  private String interfaceName;

  private String host;

  private Integer port;

  private String groupName;

  private String protocol;


}
