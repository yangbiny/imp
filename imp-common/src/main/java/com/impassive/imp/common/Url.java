package com.impassive.imp.common;

import java.util.Map;
import javax.annotation.Nullable;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
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

  private LimiterInfo limiterInfo;

  /**
   * 会用于做自适应的值
   */
  private Map<String, Object> param;

  public String buildRegistryKey(String formatTpl) {
    return String.format(
        formatTpl,
        register ? "provider" : "consumer",
        this.registryType.name().toLowerCase(),
        this.registryIp,
        this.registryPort
    );
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

  @Nullable
  public String getParam(String key) {
    if (MapUtils.isEmpty(param)) {
      return null;
    }
    return (String) param.get(key);
  }
}
