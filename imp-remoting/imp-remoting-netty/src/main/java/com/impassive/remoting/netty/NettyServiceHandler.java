package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;

/**
 * @author impassivey
 */
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
    NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
  }

  @Override
  public void connect(
      ChannelHandlerContext ctx,
      SocketAddress remoteAddress,
      SocketAddress localAddress,
      ChannelPromise promise)
      throws Exception {
    super.connect(ctx, remoteAddress, localAddress, promise);
    NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
  }

  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.disconnect(ctx, promise);
    NettyChannel.removeIfNotConnect(ctx.channel());
  }

  @Override
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.close(ctx, promise);
    NettyChannel.removeIfNotConnect(ctx.channel());
  }

  @Override
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.deregister(ctx, promise);
    NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    final NettyChannel nettyChannel =
        NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    channelHandler.receive(nettyChannel, msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
  }
}
