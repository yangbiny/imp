package com.impassive.remoting.netty.codec;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.Activity;
import com.impassive.imp.remoting.ChannelBuffer;
import com.impassive.imp.remoting.codec.AbstractCodec;
import com.impassive.imp.remoting.codec.CodecRequest;
import com.impassive.imp.remoting.codec.CodecResult;
import com.impassive.imp.util.json.JsonTools;
import com.impassive.rpc.RpcInvocation;
import com.impassive.rpc.RpcResponse;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.request.Request;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author impassivey
 */
@Activity
public class ImpCodec extends AbstractCodec {

  @Override
  public void doEncode(Url url, ChannelBuffer out, Object message) {
    boolean isRequest = message instanceof Invocation;
    out.writeBoolean(isRequest);
    if (isRequest) {
      encodeRequest(url, out, message);
      return;
    }
    encodeResponse(url, out, message);
  }

  @Override
  public Object doDecode(Url url, ChannelBuffer in) {
    boolean isRequest = in.readBoolean();
    int all = in.readInt();
    if (in.readableBytes() < all) {
      return null;
    }
    if (isRequest) {
      return decodeRequest(url, in);
    }
    return decodeResponse(url, in);
  }

  @SuppressWarnings("unchecked")
  private RpcInvocation decodeRequest(Url url, ChannelBuffer in) {
    int length = in.readInt();
    byte[] bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String serviceName = deserialize(url, bytes, String.class);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String methodName = deserialize(url, bytes, String.class);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    List<Class> classes = deserializeList(url, bytes, Class.class);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    List<CodecRequest> codecRequests = deserializeList(url, bytes, CodecRequest.class);
    Class<?>[] paramType = new Class[codecRequests.size()];
    Object[] param = new Object[codecRequests.size()];
    int index = 0;
    for (CodecRequest codecRequest : codecRequests) {
      paramType[index] = codecRequest.getClassType();
      param[index] = JsonTools.readFromJson(codecRequest.getValue(), codecRequest.getClassType());
      index++;
    }
    int isRequest = in.readInt();

    RpcInvocation rpcInvocation = new RpcInvocation();
    rpcInvocation.setMethodName(methodName);
    rpcInvocation.setParams(param);
    rpcInvocation.setParameterTypes(paramType);
    rpcInvocation.setServerName(serviceName);
    rpcInvocation.setArgumentTypes(classes.toArray(new Class[0]));
    if (isRequest == 1) {
      long requestId = in.readLong();
      rpcInvocation.setRequestId(requestId);
    }
    return rpcInvocation;
  }

  private void encodeRequest(Url url, ChannelBuffer out, Object message) {
    Invocation invocation = (Invocation) message;

    final byte[] methodNameBytes = serialize(url, invocation.getMethodName());

    final Object[] params = invocation.getParams();
    List<CodecRequest> collect = Arrays.stream(params).map(this::convertToRequestParam)
        .collect(Collectors.toList());
    final byte[] paramStrBytes = serialize(url, collect);

    final byte[] serviceNameBytes = serialize(url, invocation.getServiceName());

    final Class<?>[] argumentTypes = invocation.argumentsType();
    final List<Class> classes = Arrays.stream(argumentTypes).collect(Collectors.toList());
    final byte[] argumentTypesStrBytes = serialize(url, classes);

    int all =
        methodNameBytes.length
            + paramStrBytes.length
            + serviceNameBytes.length
            + argumentTypesStrBytes.length;
    out.writeInt(all);
    // 不是返回值
    write(out, serviceNameBytes);
    write(out, methodNameBytes);
    write(out, argumentTypesStrBytes);
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

  // response相关

  private void encodeResponse(Url url, ChannelBuffer out, Object message) {
    RpcResponse rpcResponse = (RpcResponse) message;
    Object resultValue = rpcResponse.getResult();
    Class<?> classType = null;
    if (resultValue != null) {
      classType = resultValue.getClass();
    }
    String resultJson = JsonTools.writeToJson(resultValue);
    CodecResult result = new CodecResult(rpcResponse.getRequestId(), classType, resultJson);
    byte[] bytes = serialize(url, result);
    // 继续数据的总长度
    out.writeInt(bytes.length);

    // 写入数据
    write(out, bytes);
  }

  private Object decodeResponse(Url url, ChannelBuffer in) {
    int length = in.readInt();
    byte[] bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    CodecResult codecResult = deserialize(url, bytes, CodecResult.class);
    Long requestId = codecResult.getRequestId();
    Class<?> classType = codecResult.getClassType();
    Object resultObject = JsonTools.readFromJson(codecResult.getValue(), classType);
    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setResult(resultObject);
    rpcResponse.setRequestId(requestId);
    return rpcResponse;
  }

  private void write(ChannelBuffer out, byte[] bytes) {
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }

}
