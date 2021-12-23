package com.impassive.imp.common;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

  private Boolean useEndPoint;

  private String groupName;

  private String protocol;

  private RegistryType registryType;

  private String registryIp;

  private Integer registryPort;

  private Boolean register;

  private String address;

  private ServiceRoutingType serviceRoutingType;

  public String buildRegistryKey(String formatTpl) {
    return String.format(
        formatTpl, this.registryType.name().toLowerCase(), this.registryIp, this.registryPort);
  }

  public String address() {
    if (StringUtils.isNotEmpty(address)) {
      return address;
    }
    return host + ":" + port;
  }

  public boolean useEndPoint() {
    return useEndPoint != null && useEndPoint;
  }

  public void discoverService(DiscoverService discoverService) {
    this.host = discoverService.getHost();
    this.port = discoverService.getPort();
  }
}
