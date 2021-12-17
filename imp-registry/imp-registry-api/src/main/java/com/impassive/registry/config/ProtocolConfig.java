package com.impassive.registry.config;

import com.impassive.imp.net.NetUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author impassivey
 */
@Data
public class ProtocolConfig {

  private String host;

  private Integer port;

  private Boolean useEndpoint = Boolean.FALSE;

  private String protocol = "imp";

  public boolean providerValid() {
    if (StringUtils.isEmpty(host)) {
      changeHost();
    }
    return StringUtils.isNoneEmpty(host, protocol) && port != null;
  }

  public boolean consumerValid() {
    if (useEndpoint != null && useEndpoint) {
      return StringUtils.isNoneEmpty(host, protocol) && port != null;
    }
    return true;
  }

  public void changeHost() {
    this.host = NetUtils.getAddress();
  }
}
