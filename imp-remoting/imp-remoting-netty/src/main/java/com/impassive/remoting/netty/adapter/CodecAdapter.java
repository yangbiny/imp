package com.impassive.remoting.netty.adapter;

import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.codec.Codec;
import com.impassive.rpc.result.Result;
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

  private class EncodeResponse extends MessageToByteEncoder<Result> {

    private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class)
        .getDefaultExtension();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Result result,
        ByteBuf byteBuf) {
      codec.encode(url, byteBuf, result);
    }
  }

  private class DecodeRequest extends ByteToMessageDecoder {

    private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class)
        .getDefaultExtension();

    @Override
    protected void decode(
        ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
        throws Exception {
      Object decode = codec.decode(url, byteBuf);
      list.add(decode);
    }
  }

}
