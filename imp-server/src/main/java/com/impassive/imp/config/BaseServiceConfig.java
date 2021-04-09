package com.impassive.imp.config;

import com.impassive.imp.application.ApplicationConfig;
import com.impassive.imp.protocol.ProtocolConfig;
import lombok.Getter;

/** @author impassivey */
@Getter
public class BaseServiceConfig {


  protected ProtocolConfig protocolConfig;

  protected ApplicationConfig applicationConfig;

  public void setProtocolConfig(ProtocolConfig protocol) {
    this.protocolConfig = protocol;
  }

  public void setApplicationConfig(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }
}
