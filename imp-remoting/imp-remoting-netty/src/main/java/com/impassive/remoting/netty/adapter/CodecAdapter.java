package com.impassive.remoting.netty.adapter;

import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.ChannelBuffer;
import com.impassive.imp.remoting.Codec;
import com.impassive.remoting.netty.NettyChannelBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.List;
import lombok.Getter;

@Getter
public class CodecAdapter {

  private final ChannelHandler encode = new EncodeResponse();

  private final ChannelHandler decode = new DecodeRequest();

  private final Url url;

  public CodecAdapter(Url url) {
    this.url = url;
  }

  private class EncodeResponse extends MessageToByteEncoder<Object> {

    private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class)
        .getDefaultExtension();

    @Override
    protected void encode(
        ChannelHandlerContext channelHandlerContext, Object result,
        ByteBuf byteBuf
    ) {
      ChannelBuffer channelBuffer = new NettyChannelBuffer(byteBuf);
      codec.encode(url, channelBuffer, result);
    }
  }

  private class DecodeRequest extends ByteToMessageDecoder {

    private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class)
        .getDefaultExtension();

    @Override
    protected void decode(
        ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list
    ) throws Exception {
      ChannelBuffer channelBuffer = new NettyChannelBuffer(byteBuf);
      Object decode = codec.decode(url, channelBuffer);
      list.add(decode);
    }
  }

}
