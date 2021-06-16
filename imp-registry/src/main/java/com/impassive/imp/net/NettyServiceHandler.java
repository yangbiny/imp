package com.impassive.imp.net;

import com.impassive.imp.protocol.RpcInvocation;
import com.impassive.imp.protocol.Url;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

/** @author impassivey */
@Slf4j
public class NettyServiceHandler extends ChannelDuplexHandler {

  private final ChannelHandler channelHandler;

  private final Url url;

  public NettyServiceHandler(ChannelHandler channelHandler, Url url) {
    this.channelHandler = channelHandler;
    this.url = url;
  }

  @Override
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
      throws Exception {
    super.bind(ctx, localAddress, promise);
  }

  @Override
  public void connect(
      ChannelHandlerContext ctx,
      SocketAddress remoteAddress,
      SocketAddress localAddress,
      ChannelPromise promise)
      throws Exception {
    log.info("connect : {}", ctx);
    super.connect(ctx, remoteAddress, localAddress, promise);
  }

  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.disconnect(ctx, promise);
  }

  @Override
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.close(ctx, promise);
  }

  @Override
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    log.info("register : {}", ctx);

    super.deregister(ctx, promise);
  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    log.info("write : {}", msg);
    super.write(ctx, msg, promise);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    final NettyChannel nettyChannel =
        NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    log.info("rec : {}", msg);
    channelHandler.receive(nettyChannel, msg);
  }
}
