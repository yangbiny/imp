package com.impassive.imp.protocol;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

  public void changeHost() {
    final InetAddress localHost;
    try {
      localHost = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    this.host = localHost.getHostAddress();
  }
}
