package com.impassive.rpc;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.request.Request;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Data;
import lombok.Setter;

/**
 * @author impassivey
 */
@Data
@Setter
public class RpcInvocation implements Invocation, Request {

  private static final AtomicLong ATOMIC_LONG = new AtomicLong();

  private transient Method method;

  private String methodName;

  private Object[] params;

  private String serverName;

  private Class<?>[] parameterTypes;

  private Class<?>[] argumentTypes;

  private Long requestId;

  public RpcInvocation() {
  }

  public RpcInvocation(Method method, Object[] params, String serverName) {
    this.method = method;
    this.params = params;
    this.serverName = serverName;
    if (params != null && params.length > 0) {
      this.parameterTypes = new Class[params.length];
      for (int i = 0; i < params.length; i++) {
        this.parameterTypes[i] = params[i].getClass();
      }
    }
    this.argumentTypes = method.getParameterTypes();
    this.setRequestId(ATOMIC_LONG.getAndIncrement());
  }

  @Override
  public String getServiceName() {
    return serverName;
  }

  @Override
  public String getMethodName() {
    return method == null ? methodName : method.getName();
  }

  @Override
  public Class<?>[] getParamTypes() {
    return parameterTypes;
  }

  @Override
  public Class<?>[] argumentsType() {
    return argumentTypes;
  }

  @Override
  public Object[] getParams() {
    return params;
  }

  @Override
  public void setRequestId(long id) {
    this.requestId = id;
  }

  @Override
  public Long getRequestId() {
    return this.requestId;
  }
}
