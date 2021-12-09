package com.impassive.registry.config;

import com.impassive.imp.net.NetUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/** @author impassivey */
@Getter
public class ProtocolConfig {

  private String host;

  private Integer port;

  private String protocol = "imp";

  public boolean valid() {
    if (StringUtils.isEmpty(host)) {
      changeHost();
    }
    return StringUtils.isNoneEmpty(host, protocol) && port != null;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public void setExportPort(Integer port) {
    this.port = port;
  }

  public void setHost(String host){
    this.host = host;
  }

  public void changeHost() {
    this.host = NetUtils.getAddress();
  }
}
