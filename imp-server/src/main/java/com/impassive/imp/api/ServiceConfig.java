package com.impassive.imp.api;

import com.impassive.imp.config.BaseServiceConfig;
import com.impassive.imp.protocol.ImpProtocol;
import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.InvokerWrapper;
import com.impassive.imp.proxy.JdkProxyFactory;
import com.impassive.imp.protocol.Protocol;
import com.impassive.imp.proxy.ProxyFactory;
import com.impassive.imp.common.Url;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 用于配置服务端中需要暴露出去的接口信息
 *
 * @author impassivey
 */
@Getter
public class ServiceConfig<T> extends BaseServiceConfig {

  private static final ProxyFactory PROXY_FACTORY = new JdkProxyFactory();

  private static final Protocol PROTOCOL = new ImpProtocol();

  private static volatile Boolean export = Boolean.FALSE;

  private T reference;

  /** 需要暴露出去的接口 */
  private Class<T> classInterface;

  private String interfaceName;

  private String groupName;

  public void setReference(T reference) {
    this.reference = reference;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setInterface(Class<T> classInterface) {
    if (classInterface == null || !classInterface.isInterface()) {
      throw new IllegalArgumentException("error interface info : " + classInterface);
    }
    this.classInterface = classInterface;
    this.interfaceName = classInterface.getName();
  }

  public void export() {
    checkParam();
    if (export) {
      return;
    }
    init();
    final Invoker<T> invoker = PROXY_FACTORY.getInvoker(reference, classInterface);
    final InvokerWrapper<T> wrapper = buildInvokeWrapper(invoker);
    PROTOCOL.export(wrapper);
    export = true;
  }

  private void checkParam() {
    if (applicationConfig == null) {
      throw new IllegalArgumentException("applicationConfig can not be null");
    }
    if (protocolConfig == null) {
      throw new IllegalArgumentException("protocolConfig can not be null");
    }
    if (registryConfig == null) {
      throw new IllegalArgumentException("registryConfig can not be null");
    }
    if (StringUtils.isEmpty(groupName)) {
      throw new IllegalArgumentException("groupName can not be null");
    }
    if (reference == null) {
      throw new IllegalArgumentException("reference can not be null");
    }
    if (classInterface == null) {
      throw new IllegalArgumentException("classInterface can not be null");
    }
    if (!classInterface.isInterface()) {
      throw new IllegalArgumentException(classInterface.getName() + " is not a interface");
    }
    if (!classInterface.isInstance(reference)) {
      throw new IllegalArgumentException(
          reference.getClass().getName() + "is not implement " + classInterface.getName());
    }
  }

  private void init() {
    protocolConfig.changeHost();
    if (!protocolConfig.valid()) {
      throw new RuntimeException("protocolConfig is not valid");
    }
  }

  private InvokerWrapper<T> buildInvokeWrapper(Invoker<T> invoker) {
    Url url = new Url();
    url.setHost(protocolConfig.getHost());
    url.setPort(protocolConfig.getPort());
    url.setGroupName(this.groupName);
    url.setClassType(this.classInterface);
    url.setApplicationName(applicationConfig.getApplicationName());
    url.setInterfaceName(this.interfaceName);
    url.setProtocol(protocolConfig.getProtocol());
    url.setRegistryIp(registryConfig.registryIp());
    url.setRegistryPort(registryConfig.getRegistryPort());
    url.setRegistryType(registryConfig.getRegistryType());
    url.setRegister(registryConfig.getRegister());
    return new InvokerWrapper<>(invoker, url);
  }
}
