package com.impassive.rpc;

import java.lang.reflect.Method;
import lombok.Setter;

/** @author impassivey */
@Setter
public class RpcInvocation implements Invocation {

  private transient Method method;

  private Object[] params;

  private String serverName;

  private Class<?>[] parameterTypes;

  public RpcInvocation() {}

  public RpcInvocation(Method method, Object[] params, String serverName) {
    this.method = method;
    this.params = params;
    this.serverName = serverName;
    this.parameterTypes = method.getParameterTypes();
  }

  @Override
  public String getServiceName() {
    return serverName;
  }

  @Override
  public String getMethodName() {
    return method.getName();
  }

  @Override
  public Class<?>[] getParamTypes() {
    return parameterTypes;
  }

  @Override
  public Object[] getParams() {
    return params;
  }
}
