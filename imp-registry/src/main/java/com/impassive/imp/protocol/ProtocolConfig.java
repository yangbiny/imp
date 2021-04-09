package com.impassive.imp.protocol;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Getter;

/** @author impassivey */
@Getter
public class ProtocolConfig {

  private String host;

  private Integer port;

  public void setExportPort(Integer port) {
    this.port = port;
  }

  public void changeHost() throws UnknownHostException {
    final InetAddress localHost = InetAddress.getLocalHost();
    this.host = localHost.getHostAddress();
  }
}
