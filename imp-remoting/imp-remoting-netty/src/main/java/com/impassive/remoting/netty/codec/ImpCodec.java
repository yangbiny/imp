package com.impassive.remoting.netty.codec;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.request.Request;
import com.impassive.imp.remoting.codec.AbstractCodec;
import com.impassive.imp.remoting.codec.CodecRequest;
import com.impassive.imp.remoting.codec.CodecResult;
import com.impassive.imp.util.json.JsonTools;
import com.impassive.rpc.RpcInvocation;
import com.impassive.rpc.RpcResponse;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author impassivey
 */
public class ImpCodec extends AbstractCodec {

  @Override
  public void doEncode(ByteBuf out, Object message) {
    boolean isRequest = message instanceof Invocation;
    out.writeBoolean(isRequest);
    if (isRequest) {
      encodeRequest(out, message);
      return;
    }
    encodeResponse(out, message);
  }

  @Override
  public Object doDecode(ByteBuf in) {
    boolean isRequest = in.readBoolean();
    int all = in.readInt();
    if (in.readableBytes() < all) {
      return null;
    }
    if (isRequest) {
      return decodeRequest(in);
    }
    return decodeResponse(in);
  }

  private RpcInvocation decodeRequest(ByteBuf in) {
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
    String argumentTypes = new String(bytes);

    List<Class> classes = JsonTools.readFromJsonList(argumentTypes, Class.class);

    length = in.readInt();
    bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String paramStr = new String(bytes);
    List<CodecRequest> codecRequests = JsonTools.readFromJsonList(paramStr, CodecRequest.class);
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

  private void encodeRequest(ByteBuf out, Object message) {
    Invocation invocation = (Invocation) message;

    final String methodName = invocation.getMethodName();
    final byte[] methodNameBytes = methodName.getBytes(StandardCharsets.UTF_8);

    final Object[] params = invocation.getParams();
    final List<Object> paramList = Arrays.asList(params);
    List<CodecRequest> collect = paramList.stream().map(this::convertToRequestParam)
        .collect(Collectors.toList());
    final String paramStr = JsonTools.writeToJson(collect);
    final byte[] paramStrBytes = paramStr.getBytes(StandardCharsets.UTF_8);

    final String serviceName = invocation.getServiceName();
    final byte[] serviceNameBytes = serviceName.getBytes(StandardCharsets.UTF_8);

    final Class<?>[] argumentTypes = invocation.argumentsType();
    final List<String> classes =
        Arrays.stream(argumentTypes).map(Class::getName).collect(Collectors.toList());
    final String argumentTypesStr = JsonTools.writeToJson(classes);
    final byte[] argumentTypesStrBytes = argumentTypesStr.getBytes(StandardCharsets.UTF_8);

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

  private void encodeResponse(ByteBuf out, Object message) {
    RpcResponse rpcResponse = (RpcResponse) message;
    Object resultValue = rpcResponse.getResult();
    Class<?> classType = null;
    if (resultValue != null) {
      classType = resultValue.getClass();
    }
    String resultJson = JsonTools.writeToJson(resultValue);
    CodecResult result = new CodecResult(rpcResponse.getRequestId(), classType, resultJson);
    String responseJson = JsonTools.writeToJson(result);
    byte[] bytes = responseJson.getBytes(StandardCharsets.UTF_8);
    // 继续数据的总长度
    out.writeInt(bytes.length);

    // 写入数据
    write(out, bytes);
  }

  private Object decodeResponse(ByteBuf in) {
    int length = in.readInt();
    byte[] bytes = new byte[length];
    in.readBytes(bytes, 0, length);
    String responseJson = new String(bytes);
    CodecResult codecResult = JsonTools.readFromJson(responseJson, CodecResult.class);
    Long requestId = codecResult.getRequestId();
    Class<?> classType = codecResult.getClassType();
    Object resultObject = JsonTools.readFromJson(codecResult.getValue(), classType);
    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setResult(resultObject);
    rpcResponse.setRequestId(requestId);
    return rpcResponse;
  }

  private void write(ByteBuf out, byte[] bytes) {
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }

}
