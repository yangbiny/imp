package com.impassive.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import com.impassive.imp.remoting.Invocation;
import com.impassive.imp.remoting.Request;
import com.impassive.imp.remoting.codec.AbstractCodec;
import com.impassive.imp.remoting.codec.RequestParam;
import com.impassive.imp.util.json.JsonTools;
import com.impassive.rpc.RpcInvocation;
import com.impassive.rpc.RpcResponse;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author impassivey
 */
public class ImpCodec extends AbstractCodec {

  private static final Map<String, TypeReference<List<RequestParam>>> TYPE_REFERENCE_MAP = new HashMap<>();

  @Override
  public void encode(ByteBuf out, Object message) {
    Invocation invocation = (Invocation) message;

    final String methodName = invocation.getMethodName();
    final byte[] methodNameBytes = methodName.getBytes(StandardCharsets.UTF_8);

    final Object[] params = invocation.getParams();
    final List<Object> paramList = Arrays.asList(params);
    List<RequestParam> collect = paramList.stream().map(this::convertToRequestParam)
        .collect(Collectors.toList());
    final String paramStr = JsonTools.writeToJson(collect);
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
    // 不是返回值
    out.writeInt(0);
    write(out, serviceNameBytes);
    write(out, methodNameBytes);
    write(out, paramTypeStrBytes);
    write(out, paramStrBytes);
    if (message instanceof Request) {
      Request request = (Request) message;
      Long requestId = request.getRequestId();
      out.writeInt(1);
      out.writeLong(requestId);
    } else {
      out.writeInt(0);
    }
  }

  @Override
  public Object decode(ByteBuf in) throws ClassNotFoundException {
    int all = in.readInt();
    if (in.readableBytes() < all) {
      return null;
    }
    int isResponse = in.readInt();
    if (isResponse == 1) {
      return buildResponse(in);
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

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String paramStr = new String(bytes);
    TypeReference<List<RequestParam>> typeReference = buildTypeReference();
    List<RequestParam> requestParams = JsonTools.readFromJsonList(paramStr, typeReference);
    Class<?>[] paramType = new Class[requestParams.size()];
    Object[] param = new Object[requestParams.size()];
    int index = 0;
    for (RequestParam requestParam : requestParams) {
      paramType[index] = requestParam.getClassType();
      param[index] = JsonTools.readFromJson(requestParam.getValue(), requestParam.getClassType());
      index++;
    }
    int isRequest = in.readInt();

    RpcInvocation rpcInvocation = new RpcInvocation();
    rpcInvocation.setMethodName(methodName);
    rpcInvocation.setParams(param);
    rpcInvocation.setParameterTypes(paramType);
    rpcInvocation.setServerName(serviceName);
    if (isRequest == 1) {
      long requestId = in.readLong();
      rpcInvocation.setRequestId(requestId);
    }
    return rpcInvocation;
  }

  private Object buildResponse(ByteBuf in) throws ClassNotFoundException {
    int length = in.readInt();
    byte[] bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String result = new String(bytes);

    Class<?> aClass = Class.forName(result);
    Object resultObject;
    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    result = new String(bytes);
    if (aClass == String.class) {
      resultObject = result;
    } else {
      resultObject = JSON.parse(result);
    }
    long requestId = in.readLong();
    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setResult(resultObject);
    rpcResponse.setRequestId(requestId);
    return rpcResponse;
  }

  private void write(ByteBuf out, byte[] bytes) {
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }

  private TypeReference<List<RequestParam>> buildTypeReference() {
    String s = RequestParam.class.toString();
    if (TYPE_REFERENCE_MAP.containsKey(s)) {
      return TYPE_REFERENCE_MAP.get(s);
    }
    TypeReference<List<RequestParam>> typeReference = new TypeReference<List<RequestParam>>() {
    };
    TYPE_REFERENCE_MAP.putIfAbsent(s, typeReference);
    return typeReference;
  }

}
