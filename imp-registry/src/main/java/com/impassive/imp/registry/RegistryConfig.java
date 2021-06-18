package com.impassive.imp.registry;

import com.impassive.imp.common.RegistryType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/** @author impassivey */
@Getter
public class RegistryConfig {

  private String registryAddress;

  private String registryIp;

  private Integer registryPort;

  /** 注册中心的类型 */
  private String registryTypeStr;

  /** 为true：会写入到注册中心，false不会注册 */
  private Boolean register = Boolean.FALSE;

  private RegistryType registryType;

  public void setRegistryAddress(String address) {
    this.registryAddress = address;
  }

  public void setRegistryType(String registryType) {
    this.registryTypeStr = registryType;
    this.registryType = RegistryType.of(registryTypeStr);
  }

  public void setRegister(Boolean register) {
    this.register = register;
  }

  public void setRegistryPort(Integer port) {
    this.registryPort = port;
  }

  public boolean valid() {
    if (StringUtils.isEmpty(registryTypeStr)) {
      return false;
    }
    return registryType != null && StringUtils.isNoneEmpty(registryTypeStr);
  }

  public String registryIp() {
    if (StringUtils.isEmpty(registryIp)) {
      buildRegistryIp();
    }
    return registryIp;
  }

  private void buildRegistryIp() {
    if (StringUtils.isEmpty(registryAddress)) {
      throw new IllegalArgumentException("registry address can not be null");
    }
    final String[] addressSplit = StringUtils.split(registryAddress, ":");
    if (addressSplit.length > 2) {
      throw new IllegalArgumentException("illegal registry address : " + registryAddress);
    }
    if (addressSplit.length < 2 && registryPort == null) {
      throw new IllegalArgumentException("registry port can not be null");
    }
    this.registryIp = addressSplit[0];
    if (registryPort != null) {
      return;
    }
    this.registryPort = Integer.parseInt(addressSplit[1]);
  }
}
