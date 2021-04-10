package com.impassive.imp.protocol;

import com.impassive.imp.registry.RegistryType;
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

  private RegistryType registryType;

  private String registryIp;

  private Integer registryPort;

  private Boolean register;

  public String buildRegistryKey(String formatTpl) {
    return String.format(
        formatTpl, this.registryType.name().toLowerCase(), this.registryIp, this.registryPort);
  }
}
