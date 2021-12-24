package com.impassive.config.client;

import com.impassive.imp.common.Url;
import com.impassive.protocol.ImpProtocol;
import com.impassive.proxy.JdkProxyFactory;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.protocol.Protocol;
import com.impassive.rpc.proxy.ProxyFactory;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author impassivey
 */
@Getter
public class ConsumerConfig<T> extends BaseConsumerConfig {

  private static final ProxyFactory PROXY_FACTORY = new JdkProxyFactory();

  private static final Protocol PROTOCOL = new ImpProtocol();

  private String groupName;

  private Class<T> classType;

  private String interfaceName;

  private Url url;

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setClassType(Class<T> classType) {
    if (classType != null) {
      this.interfaceName = classType.getName();
    }
    this.classType = classType;
  }

  public T refer() {
    if (url == null) {
      init();
    }
    Invoker<T> refer = PROTOCOL.refer(classType, url);
    return createProxy(refer);
  }

  private void init() {
    checkParam();
    Url url = new Url();
    url.setHost(protocolConfig.getHost());
    url.setPort(protocolConfig.getPort());
    url.setGroupName(this.groupName);
    url.setClassType(this.classType);
    url.setApplicationName(applicationConfig.getApplicationName());
    url.setInterfaceName(this.interfaceName);
    url.setProtocol(protocolConfig.getProtocol());
    url.setRegistryIp(registryConfig.registryIp());
    url.setRegistryPort(registryConfig.getRegistryPort());
    url.setRegistryType(registryConfig.getRegistryType());
    url.setRegister(registryConfig.getRegister());
    this.url = url;
  }

  private void checkParam() {
    if (applicationConfig == null) {
      throw new IllegalArgumentException("applicationConfig can not be null");
    }
    if (protocolConfig == null || !protocolConfig.consumerValid()) {
      throw new IllegalArgumentException("protocolConfig is not valid");
    }
    if (registryConfig == null) {
      throw new IllegalArgumentException("registryConfig can not be null");
    }
    if (StringUtils.isEmpty(groupName)) {
      throw new IllegalArgumentException("groupName can not be null");
    }
    if (classType == null) {
      throw new IllegalArgumentException("classInterface can not be null");
    }
    if (!classType.isInterface()) {
      throw new IllegalArgumentException(classType.getName() + " is not a interface");
    }
  }

  private T createProxy(Invoker<T> refer) {
    return PROXY_FACTORY.proxy(refer);
  }
}