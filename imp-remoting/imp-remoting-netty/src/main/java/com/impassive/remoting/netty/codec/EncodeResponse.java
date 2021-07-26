package com.impassive.remoting.netty.codec;

import com.alibaba.fastjson.JSON;
import com.impassive.imp.remoting.Result;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author impassivey
 */
public class EncodeResponse extends MessageToByteEncoder<Result> {

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Result result, ByteBuf byteBuf)
      throws Exception {
    Class<?> resultType = result.getResult().getClass();
    Object resultJson = JSON.toJSON(result.getResult());

    String resultTypeName = resultType.getName();
    String str = resultJson.toString();
    byte[] resultTypeBytes = resultTypeName
        .getBytes(StandardCharsets.UTF_8);
    byte[] resultBytes = str.getBytes(
        StandardCharsets.UTF_8);
    int all = resultTypeBytes.length + resultBytes.length;

    byteBuf.writeInt(all);
    // 是否是  返回值，1 是返回值
    byteBuf.writeInt(1);
    write(byteBuf, resultTypeBytes);
    write(byteBuf, resultBytes);
  }

  private void write(ByteBuf byteBuf, byte[] bytes) {
    byteBuf.writeInt(bytes.length);
    byteBuf.writeBytes(bytes);
  }

}
