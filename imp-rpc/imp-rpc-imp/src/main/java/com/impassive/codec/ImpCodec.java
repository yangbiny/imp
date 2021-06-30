package com.impassive.codec;

import com.google.common.base.Joiner;
import com.impassive.imp.remoting.codec.AbstractCodec;
import com.impassive.rpc.Invocation;
import com.impassive.rpc.RpcInvocation;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/** @author impassivey */
public class ImpCodec extends AbstractCodec {

  @Override
  public void encode(ByteBuf out, Object message) {
    Invocation invocation = (Invocation) message;

    final String methodName = invocation.getMethodName();
    final byte[] methodNameBytes = methodName.getBytes(StandardCharsets.UTF_8);

    final Object[] params = invocation.getParams();
    final List<Object> paramList = Arrays.asList(params);
    final String paramStr = Joiner.on(",").join(paramList);
    final byte[] paramStrBytes = paramStr.getBytes(StandardCharsets.UTF_8);

    final String serviceName = invocation.getServiceName();
    final byte[] serviceNameBytes = serviceName.getBytes(StandardCharsets.UTF_8);

    final Class<?>[] paramTypes = invocation.getParamTypes();
    final List<String> classes =
        Arrays.stream(paramTypes).map(Class::getName).collect(Collectors.toList());
    final String paramTypeStr = Joiner.on(",").join(classes);
    final byte[] paramTypeStrBytes = paramTypeStr.getBytes(StandardCharsets.UTF_8);

    int all =
        methodNameBytes.length
            + paramStrBytes.length
            + serviceNameBytes.length
            + paramTypeStrBytes.length;
    out.writeInt(all);
    write(out, serviceNameBytes);
    write(out, methodNameBytes);
    write(out, paramTypeStrBytes);
    write(out, paramStrBytes);
  }

  @Override
  public Object decode(ByteBuf in) throws ClassNotFoundException {
    int all = in.readInt();
    if (in.readableBytes() < all) {
      return null;
    }
    int length = in.readInt();
    byte[] bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String serviceName = new String(bytes);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String methodName = new String(bytes);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String paramsTypeStr = new String(bytes);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String paramStr = new String(bytes);
    Object[] param = new Object[0];
    if (StringUtils.isNotEmpty(paramStr)) {
      param = paramStr.split(",");
    }
    String[] paramTypes = new String[0];
    if (StringUtils.isNotEmpty(paramsTypeStr)) {
      paramTypes = paramsTypeStr.split(",");
    }
    Class<?>[] paramType = new Class[paramTypes.length];
    for (int i = 0; i < paramTypes.length; i++) {
      paramType[i] = Class.forName(paramTypes[i]);
    }
    RpcInvocation rpcInvocation = new RpcInvocation();
    rpcInvocation.setMethodName(methodName);
    rpcInvocation.setParams(param);
    rpcInvocation.setParameterTypes(paramType);
    rpcInvocation.setServerName(serviceName);
    return rpcInvocation;
  }

  private void write(ByteBuf out, byte[] bytes) {
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }
}
