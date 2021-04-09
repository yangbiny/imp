package com.impassive.imp.api;

import com.impassive.imp.config.BaseServiceConfig;
import com.impassive.imp.protocol.ImpProtocol;
import com.impassive.imp.protocol.Invoker;
import com.impassive.imp.protocol.JdkProxyFactory;
import com.impassive.imp.protocol.Protocol;
import com.impassive.imp.protocol.ProxyFactory;
import lombok.Getter;

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

  public void setReference(T reference) {
    this.reference = reference;
  }

  public void setInterface(Class<T> classInterface) {
    if (classInterface == null || !classInterface.isInterface()) {
      throw new IllegalArgumentException("error interface info : " + classInterface);
    }
    this.classInterface = classInterface;
  }

  public void export() {
    if (export) {
      return;
    }
    final Invoker<T> invoker = PROXY_FACTORY.getInvoker(reference, classInterface);
    PROTOCOL.export(invoker);
    export = true;
  }
}
